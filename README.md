
# Library Management REST API

## Project Description

A Library Management REST API developed using
Java, Spring Boot, Spring Data JPA and MySQL.

## Technologies Used

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL 8.4
- Maven
- Postman
- Git and GitHub

## Features

1. Book Management
2. Student Management
3. Book Issue Management
4. Book Return Management
5. Overdue Book Tracking
6. Validation and Exception Handling

## Database Configuration

Database Name: library_management

Database: MySQL 8.4

Server Port: 8081

Set the DB_PASSWORD environment variable
before starting the application.

## Run the Project

1. Clone the repository.
2. Open the project in VS Code.
3. Create the MySQL database.
4. Set the DB_PASSWORD environment variable.
5. Run the following command:

   ./mvnw spring-boot:run

## API Endpoints

### Books

- POST /api/books
- GET /api/books
- GET /api/books/{id}
- PUT /api/books/{id}
- DELETE /api/books/{id}
- GET /api/books/category/{category}

### Students

- POST /api/students
- GET /api/students
- GET /api/students/{id}
- PUT /api/students/{id}
- DELETE /api/students/{id}

### Book Issues

- POST /api/issues/{studentId}/{bookId}
- GET /api/issues
- GET /api/issues/student/{studentId}
- PUT /api/issues/return/{issueId}
- GET /api/issues/overdue