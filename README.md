# APU Automotive Service Centre Management System

## 1. Project Objective
Developed a comprehensive, role-based desktop application to streamline the daily operations of an automotive service center. The system manages the end-to-end service pipeline, handling customer appointments, inventory tracking, technician assignments, billing, and performance analytics.

## 2. Tech Stack
* **Language:** Java (JDK 21)
* **Framework:** Java Swing (Custom UI Components)
* **Storage:** Flat-file Database Architecture (TXT-based data persistence)
* **Architecture:** Object-Oriented Programming (OOP), Model-View-Controller (MVC) principles

## 3. Core Features & Capabilities
* **Role-Based Access Control (RBAC):** Secure login portals tailored for four distinct user types: Managers, Counter Staff, Technicians, and Customers, each with isolated data access and permissions.
* **Automated Job Routing:** Counter staff can actively assign pending automotive services to available technicians based on workload tracking and skill specialization.
* **Inventory & Bill of Materials (BOM):** Technicians can deduct parts used during service, which dynamically updates the centralized inventory system.
* **Payment Processing:** Integrated billing system allows counter staff to process payments (Cash, Card, Online Banking) and automatically transitions appointment statuses.
* **Executive Dashboard & Analytics:** Managers have access to real-time analytics, including revenue breakdown, active job monitoring, popular services, and employee performance tracking via data visualization charts.

## 4. Setup & Execution
1. Clone this repository to your local machine.
2. Ensure you have the Java SE 21 Development Kit (JDK) installed.
3. Import the project into your preferred Java IDE (Eclipse, IntelliJ IDEA).
4. Run `MainUI.java` located in the `src/UI/` directory to launch the application.
