# Contact List App using Java Spring Boot

This application was created to demonstrate a fully fledged full-stack application built with Spring boot + Spring Security including CRUD operations, authentication, pagination, and more.


# Layers

This is a spring boot monolithic application, it includes below layers

1. `domain` contains Entity object classes
2. `repository` communicates between database and services
3. `service` contains business logic and communicating with controller, respository and other services
4. `controller` contains Rest APIs and communicating with service layers
5. `dto` used to transfer data between layers
6. `security` central security system to authentication and authorization
7. `exception` contains central and local exception handling classes

# Security

Integration with Spring Security and add other filter for jwt token process.
The secret key is stored in `application.yml`.

# Database

It uses a PostgreSQL database (cloud database is used as default so you do not need to set local database ), can be changed easily in the `application.yml` for any other database.


# Run test

The application contains unit test cases to cover both api test and service test.

# API Documentation

[-- Swagger Document Link - -](#)
<br>Demo username: customer@mail.com<br>
Demo password: 12345

# Contact

Should you need any further information, please do not hesitate to contact the developer of this app.