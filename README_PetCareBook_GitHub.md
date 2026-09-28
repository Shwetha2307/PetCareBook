# 🐾 PetCareBook — Pet Vaccination Reminder System

A full-stack **Pet Vaccination Reminder System** developed using **Spring Boot, Spring Data JPA, MySQL, HTML, CSS and JavaScript**.

PetCareBook helps pet owners and administrators maintain pet information, vaccination records and upcoming vaccination reminders in one place.

---

## 📌 Project Overview

Pet owners can miss vaccination and deworming schedules when reminders are informal or forgotten. PetCareBook provides a centralized system to:

- Register pet owners
- Register pets
- Maintain vaccine types and their standard intervals
- Record vaccination events
- Automatically calculate the next due date
- Identify vaccinations due within the next 7 days
- View the complete vaccination history of a pet
- Monitor the system through a dashboard

The backend exposes REST APIs and the frontend is served directly by Spring Boot from the `static` resources folder.

---

## ✨ Main Features

### 📊 Dashboard
- Total pets
- Total owners
- Total vaccination records
- Upcoming vaccination reminders
- Quick navigation to major modules

### 👤 Owner Management
- Add a new owner
- View registered owners
- Store owner contact information

### 🐶 Pet Management
- Register a pet with:
  - Name
  - Species
  - Breed
  - Date of birth
  - Owner
- View registered pets
- Search pets from the frontend

### 💉 Vaccination Management
- Record a vaccination event
- Select the pet and vaccine type
- Store the vaccination date
- Automatically calculate the next due date
- View vaccination history for an individual pet

### 🧪 Vaccine Type Management
- Add vaccine types
- Define the standard vaccination interval
- Use the interval automatically when calculating the next due date

### 🔔 Reminder System
- Lists vaccinations due within the next **7 days**
- Displays upcoming due dates on the dashboard

### 🛡️ Validation & Business Rules
The application enforces the required business rules in the service layer:

- Each vaccine type has a fixed interval used to calculate the next due date.
- A vaccination record cannot be entered with a future date.
- Invalid requests return clear error messages through the global exception handler.
- Required input validation is applied to API requests.

---

## 🛠️ Technologies Used

### Backend
- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Hibernate
- Maven

### Database
- MySQL

### Frontend
- HTML5
- CSS3
- JavaScript
- Responsive dashboard UI

### Development / Testing
- IntelliJ IDEA
- Maven
- Postman or IntelliJ HTTP Client

---

## 🏗️ Project Architecture

```text
PetCareBook/
│
├── pom.xml
├── README.md
├── .gitignore
├── PetCareBook.http
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── petcarebook/
        │           ├── PetCareBookApplication.java
        │           │
        │           ├── config/
        │           │   └── DataSeeder.java
        │           │
        │           ├── controller/
        │           │   ├── OwnerController.java
        │           │   ├── PetController.java
        │           │   ├── VaccineTypeController.java
        │           │   └── VaccinationController.java
        │           │
        │           ├── dto/
        │           │   ├── DueVaccinationResponse.java
        │           │   ├── PetRequest.java
        │           │   └── VaccinationRequest.java
        │           │
        │           ├── entity/
        │           │   ├── Owner.java
        │           │   ├── Pet.java
        │           │   ├── VaccineType.java
        │           │   └── VaccinationRecord.java
        │           │
        │           ├── exception/
        │           │   ├── BusinessRuleException.java
        │           │   ├── GlobalExceptionHandler.java
        │           │   └── ResourceNotFoundException.java
        │           │
        │           ├── repository/
        │           │   ├── OwnerRepository.java
        │           │   ├── PetRepository.java
        │           │   ├── VaccineTypeRepository.java
        │           │   └── VaccinationRecordRepository.java
        │           │
        │           └── service/
        │               ├── OwnerService.java
        │               ├── PetService.java
        │               ├── VaccineTypeService.java
        │               └── VaccinationService.java
        │
        └── resources/
            ├── application.properties
            │
            └── static/
                ├── index.html
                ├── styles.css
                └── app.js
```

---

## 🗄️ Database Design

The project uses four main entities:

```text
Owner
  │
  └──< Pet
          │
          └──< VaccinationRecord >── VaccineType
```

### Owner
Stores pet owner information.

### Pet
Stores pet details and references its owner.

### VaccineType
Stores vaccine name and its standard interval.

### VaccinationRecord
Stores the vaccination event, including:

- Pet
- Vaccine type
- Date given
- Automatically calculated next due date

---

## ⚙️ Requirements

Install the following before running the project:

- **JDK 17 or later**
- **IntelliJ IDEA**
- **MySQL 8.x**
- **Maven** (IntelliJ can use the Maven configuration from `pom.xml`)

---

## 🚀 How to Run the Project

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/PetCareBook.git
cd PetCareBook
```

Replace `YOUR-USERNAME` with your GitHub username.

### 2. Open in IntelliJ IDEA

Open the project folder:

```text
PetCareBook
```

IntelliJ will detect the Maven `pom.xml` automatically.

Wait for Maven to download all dependencies.

### 3. Configure MySQL

Make sure MySQL Server is running.

The application is configured in:

```text
src/main/resources/application.properties
```

Default configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petcarebook?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Kolkata
spring.datasource.username=root
spring.datasource.password=root
```

If your MySQL password is different, change:

```properties
spring.datasource.password=root
```

to your actual MySQL password.

The database name is:

```text
petcarebook
```

The application is configured to create the database automatically if it does not already exist.

### 4. Run the Spring Boot application

Open:

```text
src/main/java/com/petcarebook/PetCareBookApplication.java
```

Click the green **Run ▶** button in IntelliJ IDEA.

### 5. Open the application

After Spring Boot starts, open:

```text
http://localhost:8080/
```

The PetCareBook frontend will load automatically.

> **No separate frontend server is required.**  
> The HTML, CSS and JavaScript frontend is served directly by Spring Boot.

---

## 🖥️ Frontend

The frontend is located at:

```text
src/main/resources/static/
```

### `index.html`
Contains the complete PetCareBook dashboard and user interface.

### `styles.css`
Contains the responsive styling, cards, forms, tables, navigation and dashboard layout.

### `app.js`
Connects the frontend to the Spring Boot REST APIs and handles:

- Dashboard loading
- Form submission
- Pet search
- Owner listing
- Vaccination records
- Upcoming reminders
- Vaccination history
- Error and success notifications

---

## 🔗 REST API Endpoints

### Owners

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/owners` | Create an owner |
| GET | `/api/owners` | Get all owners |
| GET | `/api/owners/{id}` | Get an owner by ID |

### Pets

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/pets` | Register a pet |
| GET | `/api/pets` | Get all pets |
| GET | `/api/pets/{id}` | Get a pet by ID |

### Vaccine Types

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/vaccine-types` | Create a vaccine type |
| GET | `/api/vaccine-types` | Get all vaccine types |
| GET | `/api/vaccine-types/{id}` | Get a vaccine type by ID |

### Vaccinations

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/vaccinations` | Record a vaccination |
| GET | `/api/vaccinations` | Get all vaccination records |
| GET | `/api/vaccinations/pet/{petId}` | Get a pet's vaccination history |
| GET | `/api/vaccinations/due-next-7-days` | Get vaccinations due within 7 days |

---

## 🧪 Testing

The project includes:

```text
PetCareBook.http
```

This can be opened directly in IntelliJ IDEA to test the REST APIs.

You can also test the endpoints using:

- Postman
- Insomnia
- IntelliJ HTTP Client
- The included web frontend

When testing vaccination creation, remember that the business rules reject future vaccination dates.

---

## 📸 Screenshots

Add your project screenshots here after running the application.

### Dashboard

```text
docs/screenshots/dashboard.png
```

### Pet Registration

```text
docs/screenshots/pet-registration.png
```

### Vaccination Records

```text
docs/screenshots/vaccinations.png
```

### Upcoming Reminders

```text
docs/screenshots/reminders.png
```

---

## 🔒 Important GitHub Note

Do **not** commit real passwords, API keys or other secrets to a public GitHub repository.

For a personal/local project, the current `application.properties` can be used with your local MySQL setup. For deployment, use environment variables or a separate configuration file for database credentials.

---

## 📚 Project Objective

The objective of PetCareBook is to provide a simple centralized platform for managing pet vaccination information and reducing missed vaccination schedules through automatic due-date calculation and upcoming vaccination reminders.

---

## 👨‍💻 Project Information

**Project:** PetCareBook — Pet Vaccination Reminder System

**Application Type:** Full-Stack Web Application

**Backend:** Spring Boot REST API

**Frontend:** HTML, CSS and JavaScript

**Database:** MySQL

**Language:** Java 17

---

## 📄 License

This project is intended for academic/educational use.
