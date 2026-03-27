USE studentSurvivalKit;

Create Table Student(
	student_id      INT             AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    nationality     VARCHAR(50)     NOT NULL,
    home_country    VARCHAR(50)     NOT NULL,
    program         VARCHAR(100)    NOT NULL,
    visa_type       VARCHAR(20)     NOT NULL,
    graduation_year INT             NOT NULL,
    CONSTRAINT chk_grad_year CHECK (graduation_year >= 2025)
);

CREATE TABLE Document (
    document_id         INT             AUTO_INCREMENT PRIMARY KEY,
    student_id          INT             NOT NULL,
    doc_type            VARCHAR(50)     NOT NULL,
    issue_date          DATE            NOT NULL,
    expiry_date         DATE            NOT NULL,
    issuing_authority   VARCHAR(100)    NOT NULL,
    notes               TEXT,
    CONSTRAINT fk_doc_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_doc_dates    CHECK (expiry_date > issue_date),
    CONSTRAINT uq_doc           UNIQUE (student_id, doc_type, issue_date)
);

CREATE TABLE Deadline (
    deadline_id     INT             AUTO_INCREMENT PRIMARY KEY,
    student_id      INT             NOT NULL,
    title           VARCHAR(100)    NOT NULL,
    category        VARCHAR(50)     NOT NULL,
    due_date        DATE            NOT NULL,
    reminder_date   DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'Pending',
    notes           TEXT,
    CONSTRAINT fk_deadline_student  FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_deadline_status  CHECK (status IN ('Pending', 'Completed', 'Missed')),
    CONSTRAINT chk_reminder_date    CHECK (reminder_date IS NULL OR reminder_date <= due_date),
    CONSTRAINT uq_deadline          UNIQUE (student_id, title, due_date)
);

CREATE TABLE JobApplication (
    application_id  INT             AUTO_INCREMENT PRIMARY KEY,
    student_id      INT             NOT NULL,
    company_name    VARCHAR(100)    NOT NULL,
    role            VARCHAR(100)    NOT NULL,
    location        VARCHAR(100),
    applied_date    DATE            NOT NULL,
    status          VARCHAR(30)     NOT NULL DEFAULT 'Applied',
    job_type        VARCHAR(20)     NOT NULL,
    referrals       BOOLEAN         NOT NULL DEFAULT FALSE,
    notes           TEXT,
    CONSTRAINT fk_job_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_job_status   CHECK (status IN ('Applied', 'OA', 'Phone Screen', 'Interview', 'Offer', 'Rejected', 'Withdrawn')),
    CONSTRAINT chk_job_type     CHECK (job_type IN ('Internship', 'Co-op', 'Full-time', 'Part-time')),
    CONSTRAINT uq_job           UNIQUE (student_id, company_name, role, applied_date)
);

CREATE TABLE Expense (
    expense_id      INT             AUTO_INCREMENT PRIMARY KEY,
    student_id      INT             NOT NULL,
    amount          DECIMAL(10, 2)  NOT NULL,
    category        VARCHAR(50)     NOT NULL,
    expense_date    DATE            NOT NULL,
    description     VARCHAR(200)    NOT NULL,
    currency        VARCHAR(10)     NOT NULL DEFAULT 'USD',
    CONSTRAINT fk_expense_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_amount           CHECK (amount > 0),
    CONSTRAINT chk_expense_category CHECK (category IN ('Rent', 'Food', 'Transport', 'Tuition', 'Utilities', 'Health', 'Entertainment', 'Other'))
);

CREATE TABLE Housing (
    housing_id          INT             AUTO_INCREMENT PRIMARY KEY,
    student_id          INT             NOT NULL,
    address             VARCHAR(200)    NOT NULL,
    lease_start_date    DATE            NOT NULL,
    lease_end_date      DATE            NOT NULL,
    landlord_name       VARCHAR(100)    NOT NULL,
    landlord_contact    VARCHAR(100)    NOT NULL,
    monthly_rent        DECIMAL(10, 2)  NOT NULL,
    notes               TEXT,
    CONSTRAINT fk_housing_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_lease_dates      CHECK (lease_end_date > lease_start_date),
    CONSTRAINT chk_rent             CHECK (monthly_rent > 0),
    CONSTRAINT uq_housing           UNIQUE (student_id, address, lease_start_date)
);

CREATE TABLE Recruiter (
    recruiter_id        INT             AUTO_INCREMENT PRIMARY KEY,
    application_id      INT             NOT NULL,
    recruiter_name      VARCHAR(100)    NOT NULL,
    recruiter_email     VARCHAR(100)    NOT NULL UNIQUE,
    company_name        VARCHAR(100)    NOT NULL,
    phone               VARCHAR(20),
    linkedin            VARCHAR(200),
    notes               TEXT,
    CONSTRAINT fk_recruiter_job FOREIGN KEY (application_id) REFERENCES JobApplication(application_id) ON DELETE CASCADE
);

CREATE TABLE Course (
    course_id       INT             AUTO_INCREMENT PRIMARY KEY,
    student_id      INT             NOT NULL,
    course_code     VARCHAR(20)     NOT NULL,
    course_name     VARCHAR(100)    NOT NULL,
    credits         INT             NOT NULL,
    professor       VARCHAR(100)    NOT NULL,
    semester        VARCHAR(20)     NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'In Progress',
    grade           VARCHAR(5),
    notes           TEXT,
    CONSTRAINT fk_course_student    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_credits          CHECK (credits BETWEEN 1 AND 6),
    CONSTRAINT chk_course_status    CHECK (status IN ('In Progress', 'Completed', 'Dropped')),
    CONSTRAINT uq_course            UNIQUE (student_id, course_code, semester)
);

CREATE TABLE Contact (
    contact_id      INT             AUTO_INCREMENT PRIMARY KEY,
    student_id      INT             NOT NULL,
    full_name       VARCHAR(100)    NOT NULL,
    role            VARCHAR(50)     NOT NULL,
    category        VARCHAR(30)     NOT NULL,
    email           VARCHAR(100),
    phone           VARCHAR(20),
    organization    VARCHAR(100),
    notes           TEXT,
    CONSTRAINT fk_contact_student   FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_contact_category CHECK (category IN ('DSO Advisor', 'Academic Advisor', 'Professor', 'Embassy', 'Healthcare', 'Legal', 'Other')),
    CONSTRAINT uq_contact           UNIQUE (student_id, email)
);

CREATE TABLE HealthRecord (
    health_id           INT             AUTO_INCREMENT PRIMARY KEY,
    student_id          INT             NOT NULL,
    record_type         VARCHAR(50)     NOT NULL,
    provider_name       VARCHAR(100)    NOT NULL,
    visit_date          DATE            NOT NULL,
    next_due_date       DATE,
    insurance_provider  VARCHAR(100),
    insurance_id        VARCHAR(50),
    description         TEXT,
    notes               TEXT,
    CONSTRAINT fk_health_student    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    CONSTRAINT chk_record_type      CHECK (record_type IN ('Doctor Visit', 'Vaccination', 'Dental', 'Vision', 'Insurance', 'Prescription', 'Other')),
    CONSTRAINT chk_health_dates     CHECK (next_due_date IS NULL OR next_due_date > visit_date)
);