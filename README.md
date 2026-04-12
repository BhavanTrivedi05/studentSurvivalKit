# StudentSurvivalKit

**CS 5200 — Database Management Systems | Northeastern University | Spring 2026**
**Author: Bhavan Jignesh Trivedi**

---

## What is this?

StudentSurvivalKit is a desktop application built for international students to manage everything in one place — visa documents, academic deadlines, job applications, monthly expenses, housing leases, courses, health records, and contacts. It ships with both a graphical interface (Swing GUI) and a command-line interface (CLI).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| UI | Java Swing (AWT) |
| Database | MySQL 8.0 |
| Connectivity | JDBC — mysql-connector-j-9.6.0 |
| Architecture | MVC (Model-View-Controller) |
| IDE | IntelliJ IDEA |
| DB Client | MySQL Workbench 8.0 |

---

## Requirements

Install the following before running the project:

| Software | Version | Download |
|---|---|---|
| Java JDK | 17 | https://www.microsoft.com/openjdk |
| MySQL Server | 8.0 | https://dev.mysql.com/downloads/mysql |
| MySQL Workbench | 8.0 | https://dev.mysql.com/downloads/workbench |
| IntelliJ IDEA | 2023+ | https://www.jetbrains.com/idea |
| MySQL Connector/J | 9.6.0 | https://dev.mysql.com/downloads/connector/j |

---

## Project Structure

```
StudentSurvivalKit/
├── src/
│   ├── MainGUI.java               ← Launch GUI version
│   ├── MainCLI.java               ← Launch CLI version
│   ├── db/
│   │   └── DBConnection.java      ← Singleton JDBC connection
│   ├── model/                     ← 10 model classes (one per table)
│   ├── controller/                ← 11 controllers including DashboardController
│   └── view/                      ← Swing panels, form dialogs, login screens
├── sql/
│   └── schema.sql                 ← Complete DB setup (tables + SPs + view + trigger + data)
├── lib/
│   └── mysql-connector-j-9.6.0.jar
└── README.md
```

---

## Database Setup

1. Open **MySQL Workbench** and connect to `localhost`
2. Open a new query tab
3. Open `sql/schema.sql` → paste the full contents → click **Run**
4. The script automatically:
    - Creates the `studentSurvivalKit` database
    - Creates all 10 tables with constraints
    - Creates 3 stored procedures, 1 view, and 1 trigger
    - Inserts sample data into all 10 tables
5. Verify by checking the final SELECT output — all tables should show row counts

> ⚠️ The script starts with `DROP DATABASE IF EXISTS studentSurvivalKit` — it is safe to run multiple times.

---

## Application Setup (IntelliJ)

1. Open IntelliJ IDEA → **Open** → select the `StudentSurvivalKit` folder
2. Add the JDBC connector to the classpath:
    - **File → Project Structure → Modules → Dependencies**
    - Click **+** → **JARs or Directories**
    - Select `lib/mysql-connector-j-9.6.0.jar` → OK
3. Make sure the SDK is set to **Java 17**

---

## Running the Application

### GUI (Graphical Interface)
Right-click `src/MainGUI.java` → **Run**

At the startup dialog enter:
```
URL:      jdbc:mysql://localhost:3306/studentsurvivalkit
Username: root
Password: <your MySQL root password>
```

Then sign in using:
```
Email:    bhavan.trivedi@northeastern.edu
Password: password123
```

Or click **Create Account** to register a new student.

### CLI (Command Line Interface)
Right-click `src/MainCLI.java` → **Run**

Follow the text prompts to connect, sign in, and navigate the 10 modules.

---

## Database Tables

| Table | Description |
|---|---|
| Student | Core user — email, name, visa type, program, graduation year |
| Document | Passports, visas, I-20s with expiry tracking |
| Deadline | Academic, immigration, and financial deadlines |
| JobApplication | Company, role, status, job type, referral tracking |
| Recruiter | Linked to a job application via FK |
| Expense | Categorized spending with amount, date, currency |
| Housing | Lease details, landlord info, monthly rent |
| Course | Course code, credits, professor, semester, grade |
| Contact | DSO advisors, professors, embassy contacts |
| HealthRecord | Doctor visits, vaccinations, insurance info |

---

## Database Programming Objects

| Object | Type |
|---|---|
| `get_expiring_documents` | Stored Procedure |
| `get_upcoming_deadlines` | Stored Procedure |
| `get_monthly_expense_total` | Stored Procedure |
| `student_dashboard_summary` | View |
| `trg_auto_miss_deadline` | Trigger |

---

## Sample Credentials (from schema.sql)

```
Email:    bhavan.trivedi@northeastern.edu
Password: password123

Email:    alex.chen@northeastern.edu
Password: password123
```

---

## Notes

- No `db.properties` file is needed — credentials are entered at the startup login dialog
- The application uses `PreparedStatement` throughout — no raw SQL string concatenation
- All foreign keys use `ON DELETE CASCADE ON UPDATE CASCADE`
- The trigger automatically marks deadlines as `Missed` if inserted with a past due date
- The trigger automatically marks deadlines as `Missed` if inserted with a past due date