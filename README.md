# Bank-management-system
A Java-based Bank Management System

 Overview
The Bank Management System is a comprehensive Java-based banking application that provides dual interfaces for both administrators and clients. This system simulates a real banking environment where clients can manage their accounts and transactions, while administrators can oversee branch operations and manage customer data. The application features real-time transaction processing, secure database integration, and a user-friendly interface for both user types.

[Previous sections remain the same]

# Project Demo
A complete video demonstration of the Bank Management System is available to help users understand the functionality and features. This demo walks through both client and admin interfaces, showing real-time usage of the system.

📺 Watch Demo**: [Bank Management System Demo Video](https://drive.google.com/drive/folders/1cVM0KBE2pqvqiZQi2ejXHLdVJLiXVgU6?usp=sharing)

The demo video covers:
- System setup and installation
- Client interface walkthrough
- Admin dashboard demonstration
- Account creation process
- Transaction demonstrations
- Report generation
- Other key features

This video is particularly helpful for:
- First-time users
- Setup verification
- Understanding the complete workflow
- Feature exploration

[Rest of the README remains the same]

 Features

 Client Portal
- Account Management
  - New account creation with application process
  - Account balance inquiry
  - PIN modification capabilities
  - Account statement generation

- Transaction Features
  - Fund deposits and withdrawals
  - Fund transfers between accounts
  - Real-time transaction updates
  - Complete transaction history view
  - Statement printing functionality

 Admin Portal
- Account Oversight
  - View and manage account applications
  - Access to client information
  - Account deletion capabilities
  - Real-time account status monitoring

- Branch Management
  - Transaction monitoring across accounts
  - Branch-wide report generation
  - System settings configuration
  - Data backup functionality

 Technologies Used
- Java (OpenJDK 23)
- MySQL Workbench (Database Management)
- MySQL Connector/J (Database Connectivity)
- IntelliJ IDEA (Development Environment)
- JCalendar Library

 Project Structure
- `login/` - Main entry point and authentication
- `admin/` - Administrative dashboard and functionalities
- `client/` - Client interface and transaction management
- `database/` - Database connectivity and queries
- `utils/` - Utility classes and helper functions

 Database Setup

1. Open MySQL Workbench and execute the following commands:

```sql
create database bankms;
use bankms;

-- Client Registration Tables
create table signup(
    form_no varchar(30),
    name varchar(30),
    father_name varchar(30),
    DOB varchar(30),
    gender varchar(30),
    email varchar(60),
    marital_status varchar(30),
    address varchar(60),
    city varchar(30),
    pincode varchar(30),
    state varchar(50)
);

create table signuptwo(
    form_no varchar(30),
    religion varchar(30),
    category varchar(30),
    income varchar(30),
    education varchar(30),
    occupation varchar(60),
    Pan varchar(30),
    aadhar varchar(60),
    seniorcitizen varchar(30),
    existing_account varchar(30)
);

create table signupthree(
    form_no varchar(30),
    account_type varchar(40),
    card_number varchar(30),
    pin varchar(30),
    facility varchar(200)
);

-- Authentication Tables
create table login(
    form_no varchar(30),
    card_number varchar(50),
    pin varchar(30)
);

-- Transaction Table
create table bank(
    pin varchar(10),
    date varchar(50),
    type varchar(20),
    amount varchar(20)
);

-- Admin Table
CREATE TABLE admin_users (
    admin_id varchar(30) PRIMARY KEY,
    username VARCHAR(30) UNIQUE,
    password VARCHAR(100),
    employee_name VARCHAR(50),
    email VARCHAR(50),
    phone VARCHAR(15),
    designation VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
 Prerequisites
1. OpenJDK 23 (Not included in repository)
   - Download and install from: https://openjdk.org/projects/jdk/23/
   - This is a critical requirement for running the application
   - Make sure to set up JAVA_HOME environment variable after installation

2. Development Tools
   - MySQL Workbench
   - IntelliJ IDEA (recommended)
   - MySQL Connector/J
   - JCalendar Library

 Installation & Setup

1. Install OpenJDK 23:
   - Download OpenJDK 23 from https://openjdk.org/projects/jdk/23/
   - Follow the installation instructions for your operating system
   - Verify installation by running `java -version` in terminal/command prompt

2. Clone the repository:
```bash
git clone https://github.com/svish001/Bank-management-system.git
```

3. Open the project in IntelliJ IDEA:
   - Configure project SDK to use OpenJDK 23
   - Ensure project language level is set to 23

4. Set up database connection:
   - Ensure MySQL server is running
   - Update database credentials if necessary in the connection files
   - Verify database connection

5. Configure project dependencies:
   - Add MySQL Connector/J to project libraries
   - Include JCalendar library in the project structure

2. Open the project in IntelliJ IDEA

3. Set up database connection:
   - Ensure MySQL server is running
   - Update database credentials if necessary in the connection files
   - Verify database connection

4. Configure project dependencies:
   - Add MySQL Connector/J to project libraries
   - Include JCalendar library in the project structure

 Running the Application

1. Start MySQL server and ensure database connectivity

2. Navigate to the login package and run the main class

3. For first-time use:
   - Create a new account through the client portal
   - Initial deposit is required to activate account features
   - System will generate login credentials

4. For returning users:
   - Use existing card number and PIN
   - Admin users can log in with their designated credentials

 Usage Guidelines

 For Clients
1. Account Creation:
   - Fill in personal details
   - Provide required documentation information
   - Set up security credentials
   - Make initial deposit

2. Regular Usage:
   - Login with card number and PIN
   - Access various banking features
   - View transaction history
   - Generate statements

 For Administrators
1. System Access:
   - Login with admin credentials
   - Access admin dashboard

2. Account Management:
   - Review new applications
   - Monitor transactions
   - Generate reports
   - Manage system settings

 Security Features
- Secure PIN management
- Database encryption for sensitive data
- Session management
- Access control based on user roles

 Support
For any technical issues or queries, please contact the development team or raise an issue in the repository.

 Future Enhancements
- Mobile banking integration
- Additional payment methods
- Enhanced security features
- Automated reporting system
