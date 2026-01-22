# SIRH Backend

A **Human Resources Information System** (SIRH - Système d'Information des Ressources Humaines) built with **Spring Boot** and **PostgreSQL**.

## Technology Stack

### Core Technologies

- **Java** 21  
- **Kotlin** 2.0.21  
- **Spring Boot** 4.0.1  
- **PostgreSQL** 42.6.0 (driver)  
- **Maven**  

### Main Dependencies

- Spring Data JPA  
- Spring Web  
- Spring Validation  
- Spring Boot Actuator  
- Lombok  
- MapStruct 1.5.5  
- Flyway 11.14.1  
- SpringDoc OpenAPI 3.0.1  

### Development Tools

- Spring DevTools (hot reload)  
- Asciidoctor (documentation generation)  

## Prerequisites

- **JDK** 21 or higher  
- **Maven** 3.6+  
- **PostgreSQL** 12+  
- **Git**

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd SIRH_backend
```

### 2. Create PostgreSQL database

```sql
CREATE DATABASE sirh_db;
```

### 3. Configure database connection 

File: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sirh_db
spring.datasource.username=your username
spring.datasource.password=your password
```

### 4. Build & run migrations

```bash
# Build project + download dependencies
mvn clean install

# Or just apply migrations
mvn flyway:migrate
```

```bash
mvn flyway:clean flyway:migrate
```

### 5. Run the application

```bash
mvn spring-boot:run
```

→ Application starts on: **http://localhost:8080**

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/tarmiz/SIRH_backend/
│   │   ├── SirhBackendApplication.java
│   │   ├── config/
│   │   ├── controller/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── handler/
│   │   ├── mapper/
│   │   ├── model/
│   │   │   ├── DTO/
│   │   │   └── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── util/
│   │   └── validation/
│   └── resources/
│       ├── application.properties
│       └── db/migration/
├── test/
└── docs/
```

## 🗄 Database & Flyway

Migrations are located in:  
`src/main/resources/db/migration/`

Naming convention: `V{version}__{description}.sql`

- **V1 → V98**: schema & structure
- **V99**: reference / seed data

## API Documentation

Generated automatically with **SpringDoc OpenAPI**

- Swagger UI:  
  http://localhost:8080/swagger-ui.html

- OpenAPI JSON/YAML:  
  http://localhost:8080/v3/api-docs

## Important Configuration Properties

```properties
# Database
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```