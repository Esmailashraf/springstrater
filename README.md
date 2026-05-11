# springstrater

A Spring Boot web application starter project built with Spring Boot 3.5.12 and Java 17.

## Overview

`springstrater` is a Spring Boot application using Thymeleaf templates, Spring Security, Spring Data JPA, and an embedded H2 database for runtime data persistence. It includes starter support for web pages, form validation, email sending, and developer tooling.

## Key Features

- Spring Boot 3.5.12 / Java 17
- Spring MVC web application
- Thymeleaf template rendering
- Spring Security authentication and authorization
- Spring Data JPA persistence
- H2 in-memory database runtime support
- Spring Boot Actuator for app diagnostics
- Spring Boot DevTools for local development
- Thymeleaf extras for Spring Security and Java 8 time support
- Apache Commons Lang utility library

## Project Structure

- `src/main/java` — application source code
- `src/main/resources` — application configuration, static assets, and Thymeleaf templates
- `src/test/java` — unit / integration tests
- `pom.xml` — Maven project configuration

## Getting Started

### Prerequisites

- Java 17 JDK
- Maven 3.8+ or the included Maven wrapper (`./mvnw` / `mvnw.cmd`)

### Build

Run from the project root:

```bash
./mvnw clean package
```

On Windows:

```powershell
./mvnw.cmd clean package
```

### Run

Use Maven to start the application:

```bash
./mvnw spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/springstrater-0.0.1-SNAPSHOT.jar
```

### Access

Open a browser at:

```text
http://localhost:8080
```

## Configuration

Application settings are stored in:

- `src/main/resources/application.properties`
- `src/main/resources/secret.properties`

Update these files for environment-specific values such as database, mail, or security settings.

## Notes

- `spring-boot-devtools` is included for a better development experience.
- `spring-boot-starter-mail` is available for email sending.
- `lombok` is configured as an optional dependency for compile-time annotation processing.

## Main Class

`org.studyeasy.springstrater.SpringstraterApplication`

## Useful Commands

- `./mvnw spring-boot:run` — run application locally
- `./mvnw test` — execute tests
- `./mvnw package` — build the jar

## References

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
