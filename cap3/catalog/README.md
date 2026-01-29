# Catalog API

A RESTful API for managing products and categories with a many-to-many relationship. Built with Spring Boot and following a clean layered architecture.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Exception Handling](#exception-handling)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [Sample Data](#sample-data)

## Overview

The Catalog API is a backend application that provides CRUD operations for managing products and categories. Products can belong to multiple categories, and categories can contain multiple products (Many-to-Many relationship).

### Key Highlights

- RESTful API design with proper HTTP status codes
- Layered architecture (Controller → Service → Repository → Entity)
- Global exception handling with custom error responses
- Pagination support for list endpoints
- Automatic timestamp management with JPA lifecycle callbacks
- H2 in-memory database for development and testing
- Comprehensive logging at all layers
- Sample data preloaded for testing

## Architecture

The application follows a clean **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│     Controllers Layer               │  ← HTTP Request/Response handling
├─────────────────────────────────────┤
│     Services Layer                  │  ← Business logic & transactions
├─────────────────────────────────────┤
│     Repositories Layer              │  ← Data access (Spring Data JPA)
├─────────────────────────────────────┤
│     Entities Layer                  │  ← JPA domain models
└─────────────────────────────────────┘
```

### Design Patterns

- **DTO Pattern**: Data Transfer Objects separate API contracts from domain models
- **Repository Pattern**: Spring Data JPA repositories abstract database operations
- **Service Layer Pattern**: Business logic isolated from controllers
- **Exception Handling Pattern**: Global exception handler with custom exceptions

## Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 3.5.9 | Application framework |
| Spring Data JPA | 3.5.7 | Data persistence |
| Hibernate | 6.6.39 | ORM framework |
| H2 Database | 2.3.232 | In-memory database |
| Maven | - | Build tool |
| SLF4J | 2.0.17 | Logging facade |

## Features

### Core Features

- ✅ **Complete CRUD Operations** for Products and Categories
- ✅ **Pagination Support** on list endpoints
- ✅ **Many-to-Many Relationship** between Products and Categories
- ✅ **DTO Pattern** for request/response payloads
- ✅ **Custom Exception Handling** at service layer
- ✅ **Global Exception Handler** for centralized error responses
- ✅ **Automatic Timestamps** (createdAt/updatedAt) with JPA callbacks
- ✅ **Transaction Management** with proper propagation settings
- ✅ **Comprehensive Logging** across all layers
- ✅ **H2 Console** for database inspection
- ✅ **Sample Data** preloaded on startup

### Request/Response Features

- Location header in POST responses (201 Created)
- Proper HTTP status codes (200, 201, 204, 400, 404)
- Standardized error response format
- Nested DTOs (Product includes associated Categories)

## API Endpoints

### Category Endpoints

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/categories` | List all categories (paginated) | 200 OK |
| GET | `/categories/{id}` | Get category by ID | 200 OK |
| POST | `/categories` | Create new category | 201 Created |
| PUT | `/categories/{id}` | Update category | 200 OK |
| DELETE | `/categories/{id}` | Delete category | 204 No Content |

**Request/Response Example (POST /categories):**

```json
// Request
{
  "name": "Electronics"
}

// Response (201 Created)
// Headers: Location: /categories/1
{
  "id": 1,
  "name": "Electronics"
}
```

### Product Endpoints

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/products` | List all products (paginated) | 200 OK |
| GET | `/products/{id}` | Get product by ID with categories | 200 OK |
| POST | `/products` | Create new product | 201 Created |
| PUT | `/products/{id}` | Update product and categories | 200 OK |
| DELETE | `/products/{id}` | Delete product | 204 No Content |

**Request/Response Example (GET /products/{id}):**

```json
// Response (200 OK)
{
  "id": 1,
  "name": "Smart TV 55\"",
  "date": "2024-01-15T10:30:00Z",
  "description": "4K Ultra HD Smart TV with HDR",
  "price": 2499.99,
  "imgUrl": "https://example.com/images/smart-tv.jpg",
  "categories": [
    {
      "id": 2,
      "name": "Electronics"
    }
  ]
}
```

### Pagination Parameters

Both list endpoints support pagination via query parameters:

```
GET /products?page=0&size=10&sort=name,asc
```

| Parameter | Description | Default |
|-----------|-------------|---------|
| `page` | Page number (zero-indexed) | 0 |
| `size` | Number of items per page | 20 |
| `sort` | Sort field and direction | - |

## Exception Handling

### Service Layer Exceptions

The service layer throws custom exceptions that are caught by the global exception handler:

| Exception | Thrown When | HTTP Status |
|-----------|-------------|-------------|
| `ResourceNotFoundException` | Entity not found during read/update/delete | 404 Not Found |
| `DatabaseException` | Data integrity violation during delete | 400 Bad Request |

### Global Exception Handler

Located in `com.catalog.controllers.exceptions.GlobalExceptionHandler`, this `@ControllerAdvice` provides centralized exception handling:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(...) {
        // Returns 404 with detailed error message
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(...) {
        // Returns 400 with detailed error message
    }
}
```

### Standard Error Response

All errors return a consistent JSON structure:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Entity not found with id: 123",
  "path": "/products/123"
}
```

## Project Structure

```
com.catalog/
├── CatalogApplication.java           # Spring Boot main class
│
├── controllers/                      # HTTP layer
│   ├── CategoryController.java       # Category REST endpoints
│   ├── ProductController.java        # Product REST endpoints
│   └── exceptions/
│       ├── GlobalExceptionHandler.java   # Centralized exception handling
│       └── StandardError.java            # Error response DTO
│
├── services/                         # Business logic layer
│   ├── CategoryService.java          # Category business operations
│   ├── ProductService.java           # Product business operations
│   └── exceptions/
│       ├── ResourceNotFoundException.java    # Custom exception
│       └── DatabaseException.java            # Custom exception
│
├── repositories/                     # Data access layer
│   ├── CategoryRepository.java       # Category JPA repository
│   └── ProductRepository.java        # Product JPA repository
│
├── entities/                         # Domain models
│   ├── Category.java                 # Category JPA entity
│   └── Product.java                  # Product JPA entity
│
└── dto/                              # Data transfer objects
    ├── CategoryDTO.java              # Category API payload
    └── ProductDTO.java               # Product API payload
```

### Layer Responsibilities

**Controllers:**
- Handle HTTP requests/responses
- Validate request parameters
- Delegate business logic to services
- Log HTTP operations

**Services:**
- Implement business logic
- Manage transactions
- Throw custom exceptions
- Convert entities to DTOs
- Log business operations

**Repositories:**
- Abstract database operations
- Extend Spring Data JPA interfaces
- Inherit standard CRUD methods

**Entities:**
- Map to database tables
- Define relationships
- Manage lifecycle callbacks (@PrePersist, @PreUpdate)

**DTOs:**
- Decouple API contracts from domain models
- Include conversion constructors
- Handle nested object mapping

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**

```bash
git clone <repository-url>
cd catalog
```

2. **Run with Maven**

```bash
./mvnw spring-boot:run
```

3. **Access the application**

- API Base URL: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

### H2 Console Access

```
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```

### Testing the API

**Using curl:**

```bash
# List all products
curl http://localhost:8080/products

# Get product by ID
curl http://localhost:8080/products/1

# Create new category
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"New Category"}'

# Update product
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Product","price":99.99,...}'

# Delete category
curl -X DELETE http://localhost:8080/categories/1
```

## Configuration

### Profiles

The application uses Spring profiles for environment-specific configuration:

- **test** (default): H2 in-memory database with debug logging

### Application Properties

**application.properties:**
```properties
spring.application.name=catalog
spring.profiles.active=${SPRING_PROFILES:test}
```

**application-test.properties:**

```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.open-in-view=false
spring.jpa.hibernate.ddl-auto=create-drop

# Logging
logging.level.root=INFO
logging.level.com.catalog=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### JPA Configuration

| Property | Value | Purpose |
|----------|-------|---------|
| `ddl-auto` | create-drop | Recreates schema on startup |
| `show-sql` | true | Displays SQL statements |
| `format_sql` | true | Formats SQL output |
| `open-in-view` | false | Disables OSIV anti-pattern |

## Database Schema

### Tables

**tb_category**
```sql
CREATE TABLE tb_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

**tb_product**
```sql
CREATE TABLE tb_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    date TIMESTAMP,
    description TEXT,
    price DOUBLE,
    img_url VARCHAR(255)
);
```

**tb_product_category** (Join Table)
```sql
CREATE TABLE tb_product_category (
    product_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES tb_product(id),
    FOREIGN KEY (category_id) REFERENCES tb_category(id)
);
```

### Entity Relationships

```
Product (*)----------(*)  Category
         tb_product_category
```

- One Product can have multiple Categories
- One Category can contain multiple Products
- Join table manages the Many-to-Many relationship

### Automatic Timestamps

The `Category` entity automatically manages timestamps:

- `createdAt`: Set automatically when entity is first persisted (`@PrePersist`)
- `updatedAt`: Set automatically on every update (`@PreUpdate`)

## Sample Data

The application preloads sample data from `import.sql` on startup:

### Categories
1. Livros (Books)
2. Eletrônicos (Electronics)
3. Computadores (Computers)

### Products
- 25 sample products including:
  - Books: "The Lord of the Rings"
  - Electronics: "Smart TV", "PC Gamer"
  - Computers: "Macbook Pro"
  - Technical books: "Rails for Dummies"

All products are properly associated with their respective categories.

## Logging

The application implements comprehensive logging across all layers:

### Controller Layer
```
INFO: GET /products/{id} - finding a product by id: 1
```

### Service Layer
```
INFO: Inserting new category
INFO: Deleting category with id: 1
```

### Hibernate Layer
```
DEBUG: Executing SQL query
TRACE: Binding parameter [1] as [VARCHAR] - [Electronics]
```

## Development Notes

### Code Quality Features

- **SLF4J Logging**: Structured logging across all layers
- **Transactional Boundaries**: Proper @Transactional annotations
- **Read-Only Optimization**: Read operations marked as readOnly=true
- **Entity Lifecycle Callbacks**: Automated timestamp management
- **Equals/HashCode**: Implemented based on entity ID
- **DTO Constructors**: Multiple constructors for flexible object creation

### Best Practices Implemented

✅ DTOs separate API contracts from domain models
✅ Service layer contains business logic
✅ Controllers delegate to services
✅ Custom exceptions for domain errors
✅ Global exception handler for consistent error responses
✅ Transaction management at service layer
✅ Pagination support for large datasets
✅ Proper HTTP status codes
✅ Location header in creation responses

## License

This project is part of the Java Spring Expert course by DevSuperior.

## Contributing

This is an educational project. Feel free to fork and experiment with the code.

---

**Built with Spring Boot** | **Java 21** | **Spring Data JPA**