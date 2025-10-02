# AI Tools API

A comprehensive RESTful API for managing AI bots and blogs, built with Spring Boot 3.2, PostgreSQL, JWT authentication, and Docker support.

## Features

- **JWT-based Authentication** - Secure token-based authentication
- **Public GET Endpoints** - All GET requests are publicly accessible without authentication
- **Protected Write Operations** - POST, PUT, DELETE operations require authentication
- **Bot Management** - CRUD operations for AI bot listings
- **Blog Management** - CRUD operations for blog posts
- **Multi-language Support** - Vietnamese and English content support
- **Pagination & Search** - Advanced filtering, sorting, and pagination
- **Swagger/OpenAPI Documentation** - Interactive API documentation
- **Docker Support** - Containerized deployment with Docker Compose
- **Database Migration** - Flyway for version-controlled database changes

## Technology Stack

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Security** with JWT
- **Spring Data JPA**
- **PostgreSQL 16**
- **Flyway** for database migrations
- **Maven**
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## Prerequisites

- Java 21
- Maven 3.9+
- Docker and Docker Compose
- PostgreSQL 16 (optional if using Docker)

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

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## Local Development Setup

### 1. Configure PostgreSQL

Ensure PostgreSQL is running with the following configuration:

- **Database**: `AiToooler`
- **Username**: `postgres`
- **Password**: `postgres`
- **Port**: `5432`

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

### Authentication Overview

- **All GET requests**: Public access (no authentication required)
- **POST, PUT, DELETE requests**: Require JWT authentication

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

Response:
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

### Blog Endpoints

#### Get All Blogs (Public - No Auth Required)

```bash
curl -X GET "http://localhost:8080/api/v1/blogs?lang=vi&page=0&size=10&sortBy=publishedAt&sortDir=DESC"
```

**Query Parameters:**
- `lang` (optional): Language code (`vi` or `en`, default: `vi`)
- `category` (optional): Filter by category/tag
- `keyword` (optional): Search by keyword
- `page` (optional): Page number (0-based, default: `0`)
- `size` (optional): Page size (default: `10`)
- `sortBy` (optional): Sort field (default: `publishedAt`)
- `sortDir` (optional): Sort direction (`ASC` or `DESC`, default: `DESC`)

#### Get Blog by ID (Public - No Auth Required)

```bash
curl -X GET "http://localhost:8080/api/v1/blogs/1?lang=vi"
```

#### Get Blog by Slug (Public - No Auth Required)

```bash
curl -X GET "http://localhost:8080/api/v1/blogs/slug/my-blog-post?lang=vi"
```

#### Get All Blog Categories (Public - No Auth Required)

```bash
curl -X GET http://localhost:8080/api/v1/blogs/categories
```

#### Create Blog (Protected - Requires Auth)

```bash
curl -X POST http://localhost:8080/api/v1/blogs \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "introduction-to-ai",
    "titleVi": "Giới thiệu về AI",
    "titleEn": "Introduction to AI",
    "excerptVi": "Tìm hiểu về trí tuệ nhân tạo",
    "excerptEn": "Learn about artificial intelligence",
    "contentVi": "Nội dung chi tiết...",
    "contentEn": "Detailed content...",
    "thumbnailUrl": "https://example.com/image.jpg",
    "category": "AI",
    "tags": ["AI", "Technology"],
    "publishedAt": "2024-01-01T00:00:00Z"
  }'
```

#### Update Blog (Protected - Requires Auth)

```bash
curl -X PUT http://localhost:8080/api/v1/blogs/1 \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "updated-slug",
    "titleVi": "Tiêu đề cập nhật",
    "titleEn": "Updated title",
    ...
  }'
```

#### Delete Blog (Protected - Requires Auth)

```bash
curl -X DELETE http://localhost:8080/api/v1/blogs/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Bot Endpoints

#### Get All Bots (Public - No Auth Required)

```bash
curl -X GET "http://localhost:8080/api/v1/bots?lang=vi&page=0&size=10&sortBy=createdAt&sortDir=DESC"
```

**Query Parameters:**
- `lang` (optional): Language code (`vi` or `en`, default: `vi`)
- `category` (optional): Filter by category/tag
- `keyword` (optional): Search by keyword
- `page` (optional): Page number (0-based, default: `0`)
- `size` (optional): Page size (default: `10`)
- `sortBy` (optional): Sort field (default: `createdAt`)
- `sortDir` (optional): Sort direction (`ASC` or `DESC`, default: `DESC`)

#### Get Bot by ID (Public - No Auth Required)

```bash
curl -X GET http://localhost:8080/api/v1/bots/1
```

#### Get Bot by Slug (Public - No Auth Required)

```bash
curl -X GET http://localhost:8080/api/v1/bots/slug/chatbot-ai
```

#### Get All Bot Categories (Public - No Auth Required)

```bash
curl -X GET http://localhost:8080/api/v1/bots/categories
```

#### Create Bot (Protected - Requires Auth)

```bash
curl -X POST http://localhost:8080/api/v1/bots \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "chatbot-ai",
    "nameVi": "Bot AI Chat",
    "nameEn": "AI Chat Bot",
    "descriptionVi": "Bot hỗ trợ chat tự động",
    "descriptionEn": "Automated chat support bot",
    "logoUrl": "https://example.com/logo.png",
    "category": "Customer Support",
    "tags": ["AI", "Chatbot"],
    "features": [
      {
        "titleVi": "Trả lời tự động",
        "titleEn": "Auto reply",
        "descriptionVi": "Phản hồi nhanh 24/7",
        "descriptionEn": "Quick response 24/7"
      }
    ],
    "pricing": [
      {
        "plan": "Basic",
        "price": 9.99,
        "currency": "USD",
        "features": ["Feature 1", "Feature 2"]
      }
    ]
  }'
```

#### Update Bot (Protected - Requires Auth)

```bash
curl -X PUT http://localhost:8080/api/v1/bots/1 \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "updated-slug",
    "nameVi": "Tên cập nhật",
    "nameEn": "Updated name",
    ...
  }'
```

#### Delete Bot (Protected - Requires Auth)

```bash
curl -X DELETE http://localhost:8080/api/v1/bots/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

## API Response Format

All API responses follow this standard format:

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Response data
  }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error message",
  "errors": {
    "field": "Error detail"
  }
}
```

### Paginated Response
```json
{
  "content": [
    // Array of items
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10
}
```

## Environment Configuration

### Application Properties

The application can be configured through `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/AiToooler
    username: postgres
    password: postgres

application:
  security:
    jwt:
      secret-key: your-secret-key
      expiration: 86400000  # 24 hours in milliseconds
```

### Docker Environment Variables

When running with Docker, configure via `docker-compose.yml`:

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
  POSTGRES_DB: AiToooler
  POSTGRES_USER: postgres
  POSTGRES_PASSWORD: postgres
```

## Database Schema

### Users Table

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Blogs Table

```sql
CREATE TABLE blogs (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) UNIQUE NOT NULL,
    title_vi VARCHAR(500),
    title_en VARCHAR(500),
    excerpt_vi TEXT,
    excerpt_en TEXT,
    content_vi TEXT,
    content_en TEXT,
    thumbnail_url VARCHAR(500),
    category VARCHAR(100),
    tags TEXT[],
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Bots Table

```sql
CREATE TABLE bots (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) UNIQUE NOT NULL,
    name_vi VARCHAR(255),
    name_en VARCHAR(255),
    description_vi TEXT,
    description_en TEXT,
    logo_url VARCHAR(500),
    category VARCHAR(100),
    tags TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Security Configuration

### Public Endpoints (No Authentication Required)
- All **GET** requests to any endpoint
- **POST** `/api/auth/register`
- **POST** `/api/auth/login`
- Swagger UI: `/swagger-ui/**`
- API Docs: `/v3/api-docs/**`

### Protected Endpoints (Authentication Required)
- **POST** requests (except auth endpoints)
- **PUT** requests
- **DELETE** requests

### JWT Token
- **Header**: `Authorization: Bearer <token>`
- **Expiration**: 24 hours
- **Algorithm**: HS256

## Testing

### Run all tests

```bash
mvn test
```

### Run specific test class

```bash
mvn test -Dtest=AuthServiceTest
```

### Test Coverage

- Service layer unit tests
- Controller integration tests
- Authentication flow testing
- CRUD operations testing

## Docker Commands

### Build and run

```bash
docker-compose up --build
```

### Run in detached mode

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

### Rebuild specific service

```bash
docker-compose up --build app
```

## Health Checks

Monitor application health:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Database**: Check PostgreSQL connection on port 5432

## Error Handling

The API provides comprehensive error handling:

| Status Code | Description |
|------------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request - Invalid input data |
| 401 | Unauthorized - Missing or invalid authentication |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Duplicate resource |
| 500 | Internal Server Error |

## Sample Data

The application uses Flyway migrations to seed initial data:

- Sample blogs in Vietnamese and English
- Sample bots with features and pricing
- Default user accounts for testing

## Project Structure

```
product-management-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/productapi/
│   │   │   ├── config/              # Security & app configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/          # REST controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BlogController.java
│   │   │   │   └── BotController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/              # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Blog.java
│   │   │   │   └── Bot.java
│   │   │   ├── exception/           # Custom exceptions
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── BadRequestException.java
│   │   │   ├── repository/          # Spring Data repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── BlogRepository.java
│   │   │   │   └── BotRepository.java
│   │   │   └── service/             # Business logic
│   │   │       ├── AuthService.java
│   │   │       ├── BlogService.java
│   │   │       ├── BotService.java
│   │   │       └── JwtService.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-docker.yml
│   │       └── db/migration/        # Flyway migrations
│   │           ├── V1__create_initial_tables.sql
│   │           └── V2__import_data.sql
│   └── test/                        # Unit & integration tests
│       └── java/com/example/productapi/
│           ├── controller/
│           └── service/
├── Dockerfile
├── Dockerfile.dev
├── docker-compose.yml
├── docker-compose.dev.yml
├── pom.xml
└── README.md
```

## Development Workflow

### 1. Make Changes
```bash
# Edit source code
# Run tests locally
mvn test
```

### 2. Build Application
```bash
mvn clean package
```

### 3. Run with Docker
```bash
docker-compose up --build
```

### 4. Test API
- Access Swagger UI at http://localhost:8080/swagger-ui.html
- Test endpoints using curl or Postman

## Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Database Connection Issues
```bash
# Check PostgreSQL is running
docker ps | grep postgres

# View database logs
docker-compose logs db
```

### Application Logs
```bash
# View live logs
docker-compose logs -f app

# Check log file
tail -f logs/application.log
```

## Best Practices

1. **Always use HTTPS in production**
2. **Change JWT secret key** in production environment
3. **Use environment variables** for sensitive data
4. **Implement rate limiting** for public endpoints
5. **Regular database backups**
6. **Monitor application logs**
7. **Keep dependencies updated**

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For support, bug reports, or feature requests:
- Open an issue on GitHub
- Contact: support@example.com

## Changelog

### Version 0.0.1-SNAPSHOT
- Initial release
- JWT authentication
- Blog management API
- Bot management API
- Multi-language support (Vietnamese/English)
- Public GET endpoints
- Swagger documentation
- Docker support
- Flyway database migrations
