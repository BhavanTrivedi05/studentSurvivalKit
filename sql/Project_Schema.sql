CREATE DATABASE studentSurvivalKit;
USE studentSurvivalKit;

USE studentSurvivalKit;

CREATE TABLE Student (
    student_id      INT           AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(100)  NOT NULL UNIQUE,
    first_name      VARCHAR(50)   NOT NULL,
    last_name       VARCHAR(50)   NOT NULL,
    nationality     VARCHAR(50)   NOT NULL,
    home_country    VARCHAR(50)   NOT NULL,
    program         VARCHAR(100)  NOT NULL,
    visa_type       VARCHAR(20)   NOT NULL,
    graduation_year INT           NOT NULL,
    password        VARCHAR(255)  NOT NULL DEFAULT 'changeme',
    CONSTRAINT chk_grad_year CHECK (graduation_year >= 2025)
);

CREATE TABLE Document (
    document_id       INT          AUTO_INCREMENT PRIMARY KEY,
    student_id        INT          NOT NULL,
    doc_type          VARCHAR(50)  NOT NULL,
    issue_date        DATE         NOT NULL,
    expiry_date       DATE         NOT NULL,
    issuing_authority VARCHAR(100) NOT NULL,
    notes             TEXT,
    CONSTRAINT fk_doc_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_doc_dates  CHECK (expiry_date > issue_date),
    CONSTRAINT uq_doc         UNIQUE (student_id, doc_type, issue_date)
);

CREATE TABLE Deadline (
    deadline_id   INT          AUTO_INCREMENT PRIMARY KEY,
    student_id    INT          NOT NULL,
    title         VARCHAR(100) NOT NULL,
    category      VARCHAR(50)  NOT NULL,
    due_date      DATE         NOT NULL,
    reminder_date DATE,
    status        VARCHAR(20)  NOT NULL DEFAULT 'Pending',
    notes         TEXT,
    CONSTRAINT fk_deadline_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_deadline_status CHECK (status IN ('Pending', 'Completed', 'Missed')),
    CONSTRAINT chk_reminder_date   CHECK (reminder_date IS NULL OR reminder_date <= due_date),
    CONSTRAINT uq_deadline         UNIQUE (student_id, title, due_date)
);

CREATE TABLE JobApplication (
    application_id INT          AUTO_INCREMENT PRIMARY KEY,
    student_id     INT          NOT NULL,
    company_name   VARCHAR(100) NOT NULL,
    role           VARCHAR(100) NOT NULL,
    location       VARCHAR(100),
    applied_date   DATE         NOT NULL,
    status         VARCHAR(30)  NOT NULL DEFAULT 'Applied',
    job_type       VARCHAR(20)  NOT NULL,
    referrals      BOOLEAN      NOT NULL DEFAULT FALSE,
    notes          TEXT,
    CONSTRAINT fk_job_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_job_status CHECK (status IN ('Applied', 'OA', 'Phone Screen', 'Interview', 'Offer', 'Rejected', 'Withdrawn')),
    CONSTRAINT chk_job_type   CHECK (job_type IN ('Internship', 'Co-op', 'Full-time', 'Part-time')),
    CONSTRAINT uq_job         UNIQUE (student_id, company_name, role, applied_date)
);

CREATE TABLE Expense (
    expense_id   INT            AUTO_INCREMENT PRIMARY KEY,
    student_id   INT            NOT NULL,
    amount       DECIMAL(10,2)  NOT NULL,
    category     VARCHAR(50)    NOT NULL,
    expense_date DATE           NOT NULL,
    description  VARCHAR(200)   NOT NULL,
    currency     VARCHAR(10)    NOT NULL DEFAULT 'USD',
    CONSTRAINT fk_expense_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_amount           CHECK (amount > 0),
    CONSTRAINT chk_expense_category CHECK (category IN ('Rent','Food','Transport','Tuition','Utilities','Health','Entertainment','Other'))
);

CREATE TABLE Housing (
    housing_id       INT            AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL,
    address          VARCHAR(200)   NOT NULL,
    lease_start_date DATE           NOT NULL,
    lease_end_date   DATE           NOT NULL,
    landlord_name    VARCHAR(100)   NOT NULL,
    landlord_contact VARCHAR(100)   NOT NULL,
    monthly_rent     DECIMAL(10,2)  NOT NULL,
    notes            TEXT,
    CONSTRAINT fk_housing_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_lease_dates    CHECK (lease_end_date > lease_start_date),
    CONSTRAINT chk_rent           CHECK (monthly_rent > 0),
    CONSTRAINT uq_housing         UNIQUE (student_id, address, lease_start_date)
);

CREATE TABLE Recruiter (
    recruiter_id    INT          AUTO_INCREMENT PRIMARY KEY,
    application_id  INT          NOT NULL,
    recruiter_name  VARCHAR(100) NOT NULL,
    recruiter_email VARCHAR(100) NOT NULL UNIQUE,
    company_name    VARCHAR(100) NOT NULL,
    phone           VARCHAR(20),
    linkedin        VARCHAR(200),
    notes           TEXT,
    CONSTRAINT fk_recruiter_job FOREIGN KEY (application_id) REFERENCES JobApplication(application_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Course (
    course_id   INT          AUTO_INCREMENT PRIMARY KEY,
    student_id  INT          NOT NULL,
    course_code VARCHAR(20)  NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credits     INT          NOT NULL,
    professor   VARCHAR(100) NOT NULL,
    semester    VARCHAR(20)  NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'In Progress',
    grade       VARCHAR(5),
    notes       TEXT,
    CONSTRAINT fk_course_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_credits       CHECK (credits BETWEEN 1 AND 6),
    CONSTRAINT chk_course_status CHECK (status IN ('In Progress', 'Completed', 'Dropped')),
    CONSTRAINT uq_course         UNIQUE (student_id, course_code, semester)
);

CREATE TABLE Contact (
    contact_id   INT          AUTO_INCREMENT PRIMARY KEY,
    student_id   INT          NOT NULL,
    full_name    VARCHAR(100) NOT NULL,
    role         VARCHAR(50)  NOT NULL,
    category     VARCHAR(30)  NOT NULL,
    email        VARCHAR(100),
    phone        VARCHAR(20),
    organization VARCHAR(100),
    notes        TEXT,
    CONSTRAINT fk_contact_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_contact_category CHECK (category IN ('DSO Advisor','Academic Advisor','Professor','Embassy','Healthcare','Legal','Other')),
    CONSTRAINT uq_contact           UNIQUE (student_id, email)
);

CREATE TABLE HealthRecord (
    health_id          INT          AUTO_INCREMENT PRIMARY KEY,
    student_id         INT          NOT NULL,
    record_type        VARCHAR(50)  NOT NULL,
    provider_name      VARCHAR(100) NOT NULL,
    visit_date         DATE         NOT NULL,
    next_due_date      DATE,
    insurance_provider VARCHAR(100),
    insurance_id       VARCHAR(50),
    description        TEXT,
    notes              TEXT,
    CONSTRAINT fk_health_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_record_type   CHECK (record_type IN ('Doctor Visit','Vaccination','Dental','Vision','Insurance','Prescription','Other')),
    CONSTRAINT chk_health_dates  CHECK (next_due_date IS NULL OR next_due_date > visit_date)
);

USE studentSurvivalKit;

DELIMITER //
CREATE PROCEDURE get_expiring_documents(IN p_student_id INT, IN p_days INT)
BEGIN
    SELECT document_id, doc_type, expiry_date, issuing_authority,
           DATEDIFF(expiry_date, CURDATE()) AS days_left
    FROM   Document
    WHERE  student_id = p_student_id
      AND  expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_days DAY)
    ORDER  BY expiry_date ASC;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_upcoming_deadlines(IN p_student_id INT, IN p_days INT)
BEGIN
    SELECT deadline_id, title, category, due_date,
           DATEDIFF(due_date, CURDATE()) AS days_left
    FROM   Deadline
    WHERE  student_id = p_student_id
      AND  status     = 'Pending'
      AND  due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_days DAY)
    ORDER  BY due_date ASC;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_monthly_expense_total(
    IN  p_student_id INT,
    IN  p_year       INT,
    IN  p_month      INT,
    OUT p_total      DECIMAL(10,2)
)
BEGIN
    SELECT COALESCE(SUM(amount), 0) INTO p_total
    FROM   Expense
    WHERE  student_id          = p_student_id
      AND  YEAR(expense_date)  = p_year
      AND  MONTH(expense_date) = p_month;
END //
DELIMITER ;

CREATE VIEW student_dashboard_summary AS
SELECT
    s.student_id,
    s.first_name,
    s.last_name,
    (SELECT COUNT(*) FROM Document d
     WHERE  d.student_id = s.student_id
       AND  d.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
    ) AS docs_expiring_soon,
    (SELECT COUNT(*) FROM Deadline dl
     WHERE  dl.student_id = s.student_id
       AND  dl.status     = 'Pending'
       AND  dl.due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
    ) AS deadlines_this_week,
    (SELECT COUNT(*) FROM JobApplication j
     WHERE  j.student_id = s.student_id
    ) AS total_job_applications,
    (SELECT COALESCE(SUM(amount), 0) FROM Expense e
     WHERE  e.student_id        = s.student_id
       AND  YEAR(e.expense_date)  = YEAR(CURDATE())
       AND  MONTH(e.expense_date) = MONTH(CURDATE())
    ) AS monthly_expenses
FROM Student s;

DELIMITER //
CREATE TRIGGER trg_auto_miss_deadline
BEFORE INSERT ON Deadline
FOR EACH ROW
BEGIN
    IF NEW.status = 'Pending' AND NEW.due_date < CURDATE() THEN
        SET NEW.status = 'Missed';
    END IF;
END //
DELIMITER ;

USE studentSurvivalKit;

INSERT INTO Student (email, first_name, last_name, nationality, home_country, program, visa_type, graduation_year, password) VALUES
    ('bhavan.trivedi@northeastern.edu', 'Bhavan', 'Trivedi', 'Indian',  'India', 'MS Computer Science',    'F-1', 2026, 'password123'),
    ('alex.chen@northeastern.edu',      'Alex',   'Chen',    'Chinese', 'China', 'MS Information Systems', 'F-1', 2027, 'password123');

INSERT INTO Document (student_id, doc_type, issue_date, expiry_date, issuing_authority, notes) VALUES
    (1, 'Passport', '2020-03-15', '2030-03-14', 'Indian Passport Office', 'Renewed 2020'),
    (1, 'F-1 Visa', '2023-08-01', '2026-05-15', 'US Embassy Mumbai',      'Renew before May'),
    (1, 'I-20',     '2024-01-10', '2026-12-31', 'Northeastern DSO',       'Current semester'),
    (2, 'Passport', '2021-06-20', '2031-06-19', 'Chinese Embassy',        NULL);

INSERT INTO Deadline (student_id, title, category, due_date, reminder_date, status, notes) VALUES
    (1, 'CS5200 Final Project', 'Academic',    '2026-04-10', '2026-04-05', 'Pending', 'Submit zip on Canvas'),
    (1, 'CS5800 Exam 3',        'Academic',    '2026-04-15', '2026-04-12', 'Pending', 'Graphs, DP, Greedy'),
    (1, 'OPT Application',      'Immigration', '2026-05-01', '2026-04-15', 'Pending', 'Submit through DSO'),
    (1, 'Past Deadline Test',   'Academic',    '2025-01-01', NULL,         'Pending', 'Trigger marks this Missed');

INSERT INTO JobApplication (student_id, company_name, role, location, applied_date, status, job_type, referrals, notes) VALUES
    (1, 'Google',    'SWE Intern',       'Mountain View, CA', '2026-01-10', 'Interview',    'Internship', TRUE,  'Referral from LinkedIn'),
    (1, 'Amazon',    'Backend Engineer', 'Seattle, WA',       '2026-01-20', 'OA',           'Full-time',  FALSE, NULL),
    (1, 'MathWorks', 'EDG Engineer',     'Natick, MA',        '2026-02-01', 'Phone Screen', 'Full-time',  FALSE, 'Cloud track'),
    (2, 'Microsoft', 'Data Engineer',    'Redmond, WA',       '2026-01-15', 'Applied',      'Full-time',  FALSE, NULL);

INSERT INTO Expense (student_id, amount, category, expense_date, description, currency) VALUES
    (1, 1800.00, 'Rent',      '2026-03-01', 'March rent',           'USD'),
    (1,  320.50, 'Food',      '2026-03-15', 'Groceries and dining', 'USD'),
    (1,   45.00, 'Transport', '2026-03-10', 'MBTA monthly pass',    'USD'),
    (1, 1200.00, 'Tuition',   '2026-03-20', 'Spring semester fees', 'USD');

INSERT INTO Housing (student_id, address, lease_start_date, lease_end_date, landlord_name, landlord_contact, monthly_rent, notes) VALUES
    (1, '123 Huntington Ave, Boston MA 02115', '2025-09-01', '2026-08-31', 'John Smith', 'jsmith@email.com', 1800.00, 'Utilities included'),
    (2, '45 St. Stephen St, Boston MA 02115',  '2025-08-15', '2026-08-14', 'Mary Jones', '617-555-0101',     1500.00, NULL);

INSERT INTO Course (student_id, course_code, course_name, credits, professor, semester, status, grade, notes) VALUES
    (1, 'CS5200', 'Database Management Systems', 4, 'Prof. Fontenot',    'Spring 2026', 'In Progress', NULL, 'Final project due Apr 10'),
    (1, 'CS5800', 'Algorithms',                  4, 'Prof. Tsekourakis', 'Spring 2026', 'In Progress', NULL, 'Exam 3 on Apr 15'),
    (1, 'CS5100', 'Foundations of AI',           4, 'Prof. Li',          'Fall 2025',   'Completed',   'A-', NULL),
    (2, 'IS5200', 'Database Design',             4, 'Prof. Williams',    'Spring 2026', 'In Progress', NULL, NULL);

INSERT INTO Contact (student_id, full_name, role, category, email, phone, organization, notes) VALUES
    (1, 'Dr. Sarah Kim',  'DSO Advisor',      'DSO Advisor',      'skim@northeastern.edu',     '617-373-0001', 'Northeastern ISSI',      'Primary DSO contact'),
    (1, 'Prof. Fontenot', 'Course Professor', 'Professor',        'fontenot@northeastern.edu', '617-373-0002', 'Khoury CS Dept',         'CS5200 professor'),
    (2, 'Dr. Patel',      'Academic Advisor', 'Academic Advisor', 'dpatel@northeastern.edu',   '617-373-0003', 'Khoury Graduate School', NULL);

INSERT INTO HealthRecord (student_id, record_type, provider_name, visit_date, next_due_date, insurance_provider, insurance_id, description, notes) VALUES
    (1, 'Doctor Visit', 'Northeastern Health Center', '2026-01-15', '2026-07-15', 'Aetna Student Health', 'AET-2026-001', 'Annual checkup', NULL),
    (1, 'Vaccination',  'CVS Pharmacy',               '2025-10-01', '2026-10-01', 'Aetna Student Health', 'AET-2026-001', 'Flu shot',       NULL);

INSERT INTO Recruiter (application_id, recruiter_name, recruiter_email, company_name, phone, linkedin, notes) VALUES
    (1, 'Lisa Wang', 'lwang@google.com',     'Google',    '650-555-0101', 'linkedin.com/in/lisawang', 'University recruiter'),
    (3, 'Tom Brady', 'tbrady@mathworks.com', 'MathWorks', '508-555-0202', 'linkedin.com/in/tombrady', 'EDG program recruiter');

SELECT 'Student'        AS tbl, COUNT(*) AS cnt FROM Student
UNION ALL SELECT 'Document',       COUNT(*) FROM Document
UNION ALL SELECT 'Deadline',       COUNT(*) FROM Deadline
UNION ALL SELECT 'JobApplication', COUNT(*) FROM JobApplication
UNION ALL SELECT 'Expense',        COUNT(*) FROM Expense
UNION ALL SELECT 'Housing',        COUNT(*) FROM Housing
UNION ALL SELECT 'Course',         COUNT(*) FROM Course
UNION ALL SELECT 'Contact',        COUNT(*) FROM Contact
UNION ALL SELECT 'HealthRecord',   COUNT(*) FROM HealthRecord
UNION ALL SELECT 'Recruiter',      COUNT(*) FROM Recruiter;

USE studentSurvivalKit;

DELIMITER //
CREATE PROCEDURE get_expiring_documents(IN p_student_id INT, IN p_days INT)
BEGIN
    SELECT document_id, doc_type, expiry_date, issuing_authority,
           DATEDIFF(expiry_date, CURDATE()) AS days_left
    FROM   Document
    WHERE  student_id = p_student_id
      AND  expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_days DAY)
    ORDER  BY expiry_date ASC;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_upcoming_deadlines(IN p_student_id INT, IN p_days INT)
BEGIN
    SELECT deadline_id, title, category, due_date,
           DATEDIFF(due_date, CURDATE()) AS days_left
    FROM   Deadline
    WHERE  student_id = p_student_id
      AND  status     = 'Pending'
      AND  due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_days DAY)
    ORDER  BY due_date ASC;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_monthly_expense_total(
    IN  p_student_id INT,
    IN  p_year       INT,
    IN  p_month      INT,
    OUT p_total      DECIMAL(10,2)
)
BEGIN
    SELECT COALESCE(SUM(amount), 0) INTO p_total
    FROM   Expense
    WHERE  student_id          = p_student_id
      AND  YEAR(expense_date)  = p_year
      AND  MONTH(expense_date) = p_month;
END //
DELIMITER ;