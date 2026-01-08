# 🛒 E-Commerce Application (Spring Boot – Monolithic)

A backend **e-commerce application** built using **Spring Boot** with a focus on clean architecture, secure configuration, and production-ready practices.

This project is intended for **learning, real-world backend practice, and placements**.

---

## 🚀 Tech Stack

- Java 21  
- Spring Boot 3  
- Spring Data JPA (Hibernate)  
- MySQL  
- Spring Boot Actuator  
- Gradle  
- Docker & Docker Compose  

---

## 🧩 Architecture

- **Type**: Monolithic  
- **Style**: RESTful backend  
- **Database**: MySQL  
- **Config Management**: Environment variables (`.env`)

---

## ✨ Features

- CRUD operations using JPA & Hibernate  
- MySQL database integration  
- Externalized database credentials  
- Secure Actuator endpoints  
- Graceful application shutdown  
- Docker support for local setup  
- Clean Git & SSH-based workflow  

---

## JPA Configuration
spring.jpa.database=MYSQL
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
⚠️ ddl-auto=update is for development only.
Use Flyway or Liquibase in production.

---

## ⚙️ Setup & Run

### 1️.Clone the Repository (SSH)

```bash
git clone git@github.com:<your-username>/ecommerce-monolith.git
cd ecommerce-monolith
```
### 2.Create a .env file in the project root:

DB_URL=jdbc:mysql://localhost:3306/ecomdb
DB_USERNAME=your_username
DB_PASS=your_password

### 3.Start MySQL (Docker)
```bash
docker-compose up -d
```
Ensure:
MySQL runs on port 3306
Database ecomdb exists

### 4.Run the Application
```BASH
./gradlew bootRun
```
Application runs at:
http://localhost:8080



