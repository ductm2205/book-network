# Book Social Network 📚

 A web application designed for book enthusiasts to connect, share reviews, and manage their reading list. Built with Spring Boot for the backend and Thymeleaf for server-side rendered frontend views.

This project was developed as part of the SE2 course (Group 02, Tutorial 01).

## ✨ Features

* User Registration and Login
* Browse and Search for Books
* Add Books to Personal Shelves (e.g., Read, Currently Reading, Want to Read)
* Write and Share Book Reviews/Ratings
* User management with Role Admin

## 🛠️ Technologies Used

* **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
* **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
* **Database:** MySQL
* **Database management:** PhpMyAdmin
* **Build Tool:** Maven
* **Containerization:** Docker

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

* **Java Development Kit (JDK):** Version 17 or higher. ([Download OpenJDK](https://adoptium.net/))
* **Apache Maven:** Version 3.6 or higher. ([Download Maven](https://maven.apache.org/download.cgi))
* **MySQL Server:** Version 8.0 or higher recommended. ([Download MySQL](https://dev.mysql.com/downloads/mysql/))
* **Database Management Tool (Recommended):** A tool like PhpMyAdmin, DBeaver, or MySQL Workbench to manage the database.
* **Docker & Docker Compose (Optional):** Required only if you want to run the application using Docker. ([Download Docker](https://www.docker.com/products/docker-desktop/))
* **Git:** For cloning the repository. ([Download Git](https://git-scm.com/downloads))

## 🚀 Getting Started

Follow these steps to get the application up and running locally.

### 1. Clone the Repository

```bash
git clone [https://github.com/ductm2205/book-network.git](https://github.com/ductm2205/book-network.git)
cd book-network
```
### 2. Configure Environment Variables
Create a .env file by copying the example file. Then, update the .env file with your specific configuration, especially database connection details.

```bash
cp .env.example .env
```
Now, open .env in a text editor and fill in your details (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, etc.)

### 3. Database Setup

* Ensure your MySQL server is running.
* Create the database specified in your `.env` file (e.g., `book_network_db`). You can use a command like `CREATE DATABASE book_network_db;` in a MySQL client.
* The application uses Spring Data JPA. Schema creation (tables, etc.) might be handled automatically based on your `spring.jpa.hibernate.ddl-auto` property in `application.properties` or `application.yml` (common values are `update`, `validate`, `create`, `create-drop`). Check this configuration.

## 🏃 Running the Application

You can run the application using either Maven directly or Docker Compose.

### Option 1: Using Maven

1.  **Build the application:**
    (This compiles the code, runs tests, and packages the application)
    ```bash
    mvn clean install
    ```

2.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

### Option 2: Using Docker Compose

1.  **Ensure Docker Desktop (or Docker Engine + Compose) is running.**
2.  **Build and start the services:**
    (This will build the application image and start containers for the app and potentially the database, depending on your `docker-compose.yml`)
    ```bash
    docker compose up --build
    ```
    * Use `docker compose up` for subsequent runs without rebuilding unless code changes.
    * To run in the background, use `docker compose up -d`.

*(Note: If you use Docker to run this application, your local database server must be stopped and you have to make sure that the DATABASE_PORT you config in .env is available)*

### Accessing the Application

Once the application is running successfully (either via Maven or Docker), open your web browser and navigate to:

**[http://localhost:8081](http://localhost:8081)**

*(Note: The port might differ if you changed it in your `.env` file or `application.properties`/`yml` or `docker-compose.yml`)*

#### For development purpose, you can login using:

1. Admin account:

```
email: admin@bsn.com
password: admin123
```

2. Register as a new user

### Author - GROUP 02 TUTORIAL 01

If you have any questions related to this project, contact:
[Tran Minh Duc](mailto:ductranminh12342003@gmail.com?subject=[GitHub]%20SE2%20Project%20BSN)