
# SmartHR - Human Resource Management System

SmartHR is a modern, modular, and extensible HR Management System built with **Spring Boot**. It offers a comprehensive set of features to manage employees, attendance, leaves, payroll, shifts, performance, notifications, and more—designed to simulate real-world enterprise HR operations.

---

## ✅ Completed Features

### 🎯 Core Modules
- 👤 Employee Management (CRUD, profile management)
- 🕒 Attendance Tracking (manual or auto)
- 🏖️ Leave Management (request, approval, balance tracking)
- 🧾 Payroll & Salary Management
    - Salary structure per employee
    - Auto payroll generation based on attendance & LOP
    - Payslip generation (PDF supported)
- 📆 Shift & Roster Management
    - Shift creation and assignment
    - Late entry/early leave detection
- 👨‍💼 Performance Review System
    - Quarterly/half-yearly evaluation
    - Scoring on punctuality, teamwork, problem-solving, etc.
- 🔔 Notification System
    - Notify users of leave actions, birthdays, events
    - Supports info, action, reminder categories
- 📊 Admin Dashboard
    - Leave counts, employee analytics, attendance trends

---

## 🚧 Upcoming Features

- 💬 Real-time Chat System (HR ↔ Employee communication)
- 🎓 Training & Development Management
    - Assign, track, and evaluate employee training programs
- 🌐 Multi-Tenant Architecture
    - Support multiple organizations in a single deployment
- 🛡️ Enhanced Role-based Workflow Approval Engine
    - Customizable leave, shift, and payroll approval chains
- ⚙️ Settings Module
    - HR can configure working days, holidays, etc.
- 📂 Document Upload System
    - Upload resume, certificates, appraisal letters, etc.
- 🧠 AI-driven Performance Insights (long-term goal)

---

## ⚙️ Technologies Used

- Java 21
- Spring Boot 3.5+
- Spring Security (JWT-based authentication)
- JPA (Hibernate)
- PostgreSQL/MySQL
- Maven
- Lombok, Jackson, JasperReports (PDF), WebSocket (planned)

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/roze/smarthr/
│   │   ├── config/             # Security & App Configs
│   │   ├── constant/           # Constants
│   │   ├── controller/         # REST Controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── entity/             # JPA Entities
│   │   ├── enums/              # Enums
│   │   ├── exception/          # Custom Exceptions
│   │   ├── mapper/             # DTO-Entity Mappers
│   │   ├── repository/         # Spring Data JPA Repositories
│   │   ├── security/           # Security Logic
│   │   ├── service/            # Business Services
│   │   └── SmartHrApplication.java
│   └── resources/
│       └── application.yml
└── test/                       # Unit/Integration Tests
```

---

## 🛠️ How to Run

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL or PostgreSQL

### Steps

```bash
# Clone the repo
git clone https://github.com/firoze-hossain/SmartHR.git
cd SmartHR

# Update application.yml for DB config

# Build and run
mvn clean spring-boot:run
```

---

## 🗂️ API Modules Overview

| Module            | Description                                      |
|-------------------|--------------------------------------------------|
| Employee          | Register, update, and manage employee data       |
| Attendance        | Daily check-in/out & status tracking             |
| Leave             | Leave types, request, approval, balance logic    |
| Payroll           | Salary structure, LOP, payslip generation        |
| Shift & Roster    | Define shifts and assign to employees            |
| Notification      | Alerts and system messages                       |
| Dashboard         | Summary stats, counts, and charts                |
| Performance       | Periodic evaluations and feedback                |

---

## 🤝 Contribution

Contributions are welcome! Feel free to fork and open a PR.

1. Fork this repository
2. Create your branch: `git checkout -b feature/feature-name`
3. Commit your changes: `git commit -am 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Open a Pull Request

---

## 👨‍💻 Developed By

Md. Firoze Hossain  
[GitHub: firoze-hossain](https://github.com/firoze-hossain)  
Java & Spring Boot Developer | United Hospital Ltd.

---

© 2025 SmartHR. All rights reserved.