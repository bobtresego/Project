# Inventory Management System

## 1. Project Overview

The Inventory Management System is a Java desktop application developed to help businesses manage products, monitor stock levels, record sales, and generate basic inventory reports.

The system was developed using **Java Swing** for the graphical user interface and **MySQL** for database storage. It aims to reduce errors associated with manual, paper-based record keeping by providing a centralised digital system for inventory management.

This project was developed as part of a third-year group project.

## 2. Key Features

- **User Registration and Approval:** New users can register and must be approved by an administrator before accessing the system.
- **Secure Login Workflow:** Login access is controlled according to user status, including Pending, Approved, and Blocked.
- **Product Management:** Add, view, update, and delete product records.
- **Stock Management:** Add or remove stock and update product prices.
- **Sales Processing:** Record sales and automatically deduct sold quantities from available stock.
- **Role-Based Access Control:** Separate access permissions for administrators and staff members.
- **User Management:** Administrators can approve, reject, block, or delete user accounts.
- **Inventory Reports:** View inventory information and identify products with low stock levels.

## 3. Technologies Used

- **Programming Language:** Java
- **Graphical User Interface:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **Development Environment:** Java-compatible IDE
- **Version Control:** Git and GitHub

## 4. System Requirements

Before running the application, ensure that you have:

- Java Development Kit (JDK) installed.
- MySQL Server installed and running.
- A MySQL database configured for the application.
- The MySQL JDBC driver available to the project.
- The project source code and required dependencies.

## 5. Installation and Setup

### Step 1: Clone the Repository

Clone the repository to your local computer:

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

Alternatively, download the repository as a ZIP file and extract it.

### Step 2: Configure MySQL

1. Start your MySQL server.
2. Create the database required by the application.
3. Execute the provided SQL script, if included, to create the required tables.
4. Ensure that the database structure matches the application's requirements.

### Step 3: Configure the Database Connection

Locate `DBConnection.java` in the database package.

Configure the database connection URL, username, and password to match your local MySQL setup.

**Important:** Do not publish real database passwords or other sensitive credentials in the repository.

### Step 4: Open the Project

1. Open your Java IDE.
2. Import or open the project folder.
3. Ensure that the MySQL JDBC driver is included in the project dependencies.
4. Build the project and resolve any missing dependency errors.

### Step 5: Run the Application

Run the application's main class from your IDE.

Log in using an approved account configured for the demonstration, or register a new account and have an administrator approve it.

## 6. User Roles

### Administrator

Administrators can manage products, monitor inventory, process sales where permitted, manage user accounts, and access system reports.

### Staff

Staff members have access to the inventory and sales functions permitted by their assigned role.

Access to restricted administrative functions is controlled by the application.

## 7. Sales and Inventory Processing

When a sale is recorded, the application checks stock availability and updates the inventory accordingly.

The sales process uses a JDBC database transaction to help ensure that stock changes are committed together or rolled back if an error occurs.

This helps prevent incomplete sales transactions and unauthorised overselling.

## 8. Project Structure

The project is organised into packages that separate the user interface, controllers, models, and database connectivity.

The repository submission may also contain the following supporting folders:

- `Code/` — Application source code and related project files.
- `Demo/` — Demonstration video or a document containing its link.
- `Dox/` — Project documentation and final report.
- `Group4-Names.txt` — Group member names.

The exact folder structure depends on the submitted repository.

## 9. Testing

The application should be tested against the following scenarios:

- User registration and administrator approval.
- Login attempts for Pending, Approved, and Blocked accounts.
- Administrator and staff access restrictions.
- Product creation, updates, and deletion.
- Stock additions and removals.
- Successful sales and automatic stock deduction.
- Prevention of sales exceeding available stock.
- Low-stock identification in inventory reports.
- Handling of invalid inputs and database errors.

## 10. Known Limitations

The current implementation has several limitations:

- Passwords are not securely hashed in the current implementation.
- Sales are limited to one product per transaction.
- Barcode scanning and offline transaction buffering are not implemented.
- Reporting is limited to the currently implemented inventory reports and does not include advanced analytics charts.
- Database credentials require improved configuration and secret management before production deployment.

These limitations provide opportunities for future development.

## 11. Future Improvements

Potential improvements include:

- Password hashing using a suitable password-hashing algorithm.
- Multi-product shopping cart functionality.
- Barcode scanning integration.
- Improved database credential management.
- Offline transaction support and synchronisation.
- More detailed sales analytics and visual reports.
- Expanded automated testing.

## 12. Project Demonstration

A demonstration video and the final project report should be included in the submission folders or linked below.

- **Demo Video:** [Insert demonstration video link]
- **Final Report:** [Insert report path or link]
- **GitHub Repository:** [Insert repository URL]

Ensure that the demonstration video and supporting documents are accessible to the lecturer or examiner.

## 13. Acknowledgements

This project was developed collaboratively as part of a third-year university group assignment. It demonstrates the application of Java programming, database connectivity, inventory management principles, and software development practices.

