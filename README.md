# Product Management API

A comprehensive RESTful API for product management built with Spring Boot 3.2, PostgreSQL, JWT authentication, and Docker support.

## Features

- JWT-based authentication
- User registration and login
- CRUD operations for products
- Role-based access control
- Pagination, sorting, and search functionality
- Swagger/OpenAPI documentation
- Docker containerization
- Comprehensive error handling
- Unit and integration testing

## Technology Stack

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Security** with JWT
- **Spring Data JPA**
- **PostgreSQL 16**
- **Maven**
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## Prerequisites

- Java 21
- Maven 3.9+
- Docker and Docker Compose
- PostgreSQL (optional if using Docker)

## Quick Start with Docker

### 1. Clone the repository
```bash
git clone <repository-url>
cd product-management-api
```

### 2. Run with Docker Compose
```bash
docker-compose up --build
```

The application will be available at:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

### 3. Default Credentials
- Username: `admin`
- Password: `admin123`

## Local Development Setup

### 1. Install PostgreSQL
Make sure PostgreSQL is running on port 5432 with:
- Database: `productdb`
- Username: `postgres`
- Password: `postgres`

### 2. Build the application
```bash
mvn clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/product-management-api-0.0.1-SNAPSHOT.jar
```

## API Documentation

### Authentication Endpoints

#### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "success": true,
    "message": "Login successful",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

### Product Endpoints (Protected - Requires JWT Token)

#### Get All Products (with Pagination and Search)
```bash
curl -X GET "http://localhost:8080/api/products?page=0&size=10&sortBy=createdAt&sortDirection=DESC&name=laptop&category=Electronics" \
  -H "Authorization: Bearer <your-jwt-token>"
```

#### Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>"
```

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "quantity": 10,
    "category": "Electronics",
    "imageUrl": "https://example.com/laptop.jpg"
  }'
```

#### Update Product
```bash
curl -X PUT http://localhost:8080/api/products/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description",
    "price": 1199.99,
    "quantity": 15,
    "category": "Electronics",
    "imageUrl": "https://example.com/updated-laptop.jpg"
  }'
```

#### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>"
```

## API Response Format

All API responses follow this format:
```json
{
  "success": true/false,
  "message": "Response message",
  "data": {
    // Response data
  }
}
```

## Pagination Parameters

- `page`: Page number (0-based, default: 0)
- `size`: Page size (default: 10)
- `sortBy`: Field to sort by (default: createdAt)
- `sortDirection`: ASC or DESC (default: DESC)

## Search Parameters

- `name`: Search products by name (partial match)
- `category`: Filter products by category (partial match)

## Environment Configuration

### Application Properties
The application can be configured through `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb
    username: postgres
    password: postgres

application:
  security:
    jwt:
      secret-key: your-secret-key
      expiration: 86400000  # 24 hours
```

### Docker Environment Variables
When running with Docker, you can override settings using environment variables:

```yaml
SPRING_PROFILES_ACTIVE: docker
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Products Table
```sql
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER DEFAULT 0,
    category VARCHAR(100),
    image_url VARCHAR(500),
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Testing

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=AuthServiceTest
```

### Test coverage includes:
- Service layer unit tests
- Controller integration tests
- Authentication flow testing
- CRUD operations testing

## Docker Commands

### Build and run
```bash
docker-compose up --build
```

### Run in background
```bash
docker-compose up -d
```

### Stop containers
```bash
docker-compose down
```

### Remove containers and volumes
```bash
docker-compose down -v
```

### View logs
```bash
docker-compose logs -f app
```

## Health Checks

The application includes health check endpoints:
- Docker health check: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Security Features

- BCrypt password encoding
- JWT token authentication
- Role-based access control (USER, ADMIN)
- Token expiration (24 hours)
- Secured endpoints with Spring Security

## Error Handling

The API includes comprehensive error handling for:
- 400 Bad Request - Invalid input data
- 401 Unauthorized - Invalid or missing authentication
- 403 Forbidden - Insufficient permissions
- 404 Not Found - Resource not found
- 500 Internal Server Error - Server errors

## Sample Data

The application automatically seeds the database with:
- 1 Admin user (username: `admin`, password: `admin123`)
- 5 Sample products in various categories

## Project Structure

```
product-management-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/productapi/
│   │   │   ├── config/         # Security and configuration
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── dto/           # Request/Response DTOs
│   │   │   ├── entity/        # JPA entities
│   │   │   ├── exception/     # Custom exceptions
│   │   │   ├── repository/    # Data repositories
│   │   │   └── service/       # Business logic
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   └── test/                   # Unit and integration tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For support and questions, please contact support@example.com#   B l o g - A i  
 