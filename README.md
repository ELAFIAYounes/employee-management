# Employee Records Management System

A comprehensive system for managing employee records with a Swing-based desktop UI and RESTful API backend.

## Features

- Employee data management (CRUD operations)
- Role-based access control (HR, Managers, Administrators)
- Audit trail for all changes with user tracking and IP logging
- Advanced search and filtering capabilities
- Comprehensive data validation
- Reporting functionality
- Department-based access restrictions
- Version control for employee records

## Technology Stack

- Java 17
- Spring Boot 3.2
- Oracle Database
- Swing UI with MigLayout
- Docker containerization
- JUnit and Mockito for testing
- Swagger/OpenAPI documentation
- Spring Security with JWT authentication

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven 3.8+
- Oracle Database (or Oracle XE)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/employee-management.git
   cd employee-management
   ```

2. Configure the database:
   - Create an Oracle user and database (see database-setup.sql)
   - Update application.properties with your database credentials

3. Build the application:
   ```bash
   mvn clean package
   ```

4. Start the containers:
   ```bash
   docker-compose up -d
   ```

5. Access the application:
   - Swing UI will start automatically
   - API documentation: http://localhost:8085/swagger-ui.html
   - Default admin credentials: admin/admin123

## Project Structure

```
employee-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/employee/
│   │   │       ├── api/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ui/
│   │   └── resources/
│   └── test/
├── docker/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## API Documentation

The API is fully documented using OpenAPI/Swagger. Access the documentation at http://localhost:8085/swagger-ui.html when the application is running.

## Security Features

- Role-based access control (HR, Managers, Administrators)
- Department-based access restrictions
- Spring Security integration
- JWT-based authentication
- Secure password storage with BCrypt
- Comprehensive audit logging
- IP tracking for all operations

## Testing

Run the tests using:
```bash
mvn test
```

For integration tests:
```bash
mvn verify
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- Oracle for database support
- All contributors who participate in this project
