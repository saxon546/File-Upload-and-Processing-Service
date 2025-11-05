# File Upload & Processing Service (Spring Boot)

A Spring Boot service that allows users to upload `.txt` or `.csv` files, validates them, counts lines and words, and stores the results in an in‑memory database.

---

## Features
- Upload `.txt` and `.csv` files via REST API  
- Validate file type and reject unsupported uploads  
- **Dual‑mode processing**:
  - `.txt` → words counted by whitespace separation  
  - `.csv` → words counted by commas **and** whitespace  
- Count number of lines and words in the file  
- Store processed results in an in‑memory database (thread‑safe)  
- Retrieve processed file results by ID or list all results  
- Structured error responses with Spring Boot’s `ProblemDetail`  
- Logging of all known exceptions  

---

## Tech Stack
- **Java 17+**  
- **Spring Boot 3.x** (Web, Validation, Logging)  
- **Maven** for build and dependency management  

---

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/file-service.git

### 2. Build the project
```bash
mvn clean package

### 3. Run the service
```bash
mvn spring-boot:run
