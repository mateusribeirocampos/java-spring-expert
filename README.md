# Java Spring Expert — Backend Study Repository

> Advanced Java Spring Boot study focused on **software testing**, **security**, **query optimization**, and **best practices** for production-grade REST APIs.

---

## Overview

This repository contains a series of projects built throughout an advanced Java Spring Boot curriculum. The main goal is to go beyond basic CRUD — covering automated testing at all levels, OAuth2/JWT security, advanced JPA queries, code coverage analysis, and API testing with industry-standard tools.

**Key focus areas:**
- Automated testing: unit, integration, and API
- Test-Driven Development (TDD)
- Spring Security with OAuth2 + JWT
- JPA query optimization (N+1 problem, JOIN FETCH, 2-query strategy)
- Code coverage with JaCoCo
- API testing with MockMvc and RestAssured

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Persistence | Spring Data JPA / Hibernate |
| Database | H2 (test) · PostgreSQL (dev/prod) |
| Security | Spring Security · Spring OAuth2 Authorization Server · JWT (RSA) |
| Testing | JUnit 5 · Mockito · MockMvc · RestAssured |
| Coverage | JaCoCo |
| Build | Maven |
| Validation | Bean Validation (jakarta) |
| Mapping | DTO pattern |
| Docs/Test | Postman |

---

## Repository Structure

```
spring-boot-expert/
├── catalog/                      # DSCatalog — main project (all chapters)
├── cap3/
│   ├── empregados-tdd-adr/       # TDD challenge starter (Employee-Department)
│   ├── desafio-empregados-cap3/  # TDD challenge solution (Employee-Department)
│   └── catalog/                  # DSCatalog snapshot — cap 3 (security)
├── cap4/
│   └── desafio-movieflix/        # MovieFlix challenge (queries + security)
├── cap6/
│   └── dscommerce-mockmvc-cap6/  # DSCommerce — MockMvc & RestAssured tests
├── class-junit/                  # JUnit classroom examples
├── exercicies-junit-vanilla/     # JUnit vanilla exercises (Financing domain)
└── docs/
    └── docs-curso/               # Chapter guides and competency lists
```

---

## Projects

### DSCatalog — Main Project

Product catalog REST API built progressively across all chapters.

**Domain:** Categories → Products (N-N) | Users → Roles (N-N)

**Implemented features:**
- Full CRUD with layered architecture (Controller → Service → Repository)
- Pagination with `Pageable`, audit fields (`@PrePersist`, `@PreUpdate`)
- Bean Validation with custom `ConstraintValidator` (DB-aware validation)
- Spring Security with OAuth2 Authorization Server + Resource Server
- JWT (RSA-signed) with custom claims via `tokenCustomizer()`
- Method-level authorization with `@PreAuthorize`
- Advanced product search: name filter + category filter, paginated
- N+1 problem solved via 2-query strategy (native SQL + JPQL JOIN FETCH)
- Full test suite: unit (Service, Repository) + integration + API (MockMvc + RestAssured)
- JaCoCo coverage: **100% instructions and branches** across all services

**Key endpoints:**

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/oauth2/token` | Basic (client) | Obtain JWT |
| `GET` | `/products?name=&categoryId=` | — | Paginated product search |
| `GET` | `/products/{id}` | — | Product details with categories |
| `POST` | `/products` | ADMIN | Insert product |
| `PUT` | `/products/{id}` | ADMIN | Update product |
| `DELETE`| `/products/{id}` | ADMIN | Delete product |
| `GET` | `/categories` | — | List categories |
| `GET` | `/users/me` | Any auth | Current user profile |
| `POST` | `/users` | — | Sign up |

---

### Event-City TDD (Chapter 2 Challenge)

Relationship-focused TDD challenge. Tests were **pre-written as specification** — the task was implementing all features to make them pass.

**Domain:** `City` ← `Event` (N-1)

**Result:** 7/7 tests passing (100%)

**Skills demonstrated:**
- TDD reverse (test-first implementation)
- Exception handling with `@ControllerAdvice`
- Referential integrity constraint handling
- Correct HTTP status codes (200, 201, 204, 400, 404, 422)

---

### Employee-Department (Chapter 3 Challenge)

Security + Validation challenge. Added OAuth2/JWT and Bean Validation to a pre-existing TDD project.

**Domain:** `Department` ← `Employee` (N-1)

**Result:** 12/12 integration tests passing (100%)

**Skills demonstrated:**
- OAuth2 Authorization Server + Resource Server configuration
- Token-based auth in integration tests (`TokenUtil`)
- Bean Validation (`@NotBlank`, `@NotNull`, `@Email`, `@Positive`)
- Bug diagnosis: `@NotBlank` vs `@NotNull` on numeric (`Long`) fields

---

### MovieFlix (Chapter 4 Challenge)

Movie catalog with reviews. Full domain from scratch.

**Domain:** `Genre` → `Movie` (N-1) | `Movie` ← `Review` → `User`

**Skills demonstrated:**
- JPQL with `JOIN FETCH` for single-item queries
- 2-query strategy for paginated lists with category filter
- `@ManyToOne` lazy loading problem and solution
- Custom security rule: only `MEMBER` role can post reviews
- `@PreAuthorize` with authenticated user context

---

### DSCommerce — Jacoco + API Tests (Chapters 5 & 6)

E-commerce order system used as the subject for coverage analysis and advanced API testing.

**Domain:** `Product` ← `OrderItem` → `Order` → `User`

**JaCoCo Results (Chapter 5):**

| Service | Instructions | Branches |
|---------|:-----------:|:--------:|
| `CategoryService` | 100% | 100% |
| `ProductService` | 100% | 100% |
| `UserService` | 100% | 100% |
| `AuthService` | 100% | 100% |
| `OrderService` | 100% | 100% |

**MockMvc Tests (Chapter 6) — `ProductControllerIT`:**
- `GET /products?name=` — 2 scenarios (200 OK)
- `POST /products` — 8 scenarios (201, 422×5, 403, 401)
- `DELETE /products/{id}` — 5 scenarios (204, 404, 400, 403, 401)
- `GET /orders/{id}` — 6 scenarios (200, 200, 403, 404, 404, 401)

---

### DSMovie — RestAssured Challenge (Chapter 6 Challenge)

Movie rating system used for the final API testing challenge.

**Domain:** `Movie` ← `Score` → `User` (with computed averages)

**RestAssured Results:**

| Test Class | Tests | Result |
|------------|:-----:|:------:|
| `MovieControllerRA` | 7 | ✅ All passing |
| `ScoreControllerRA` | 3 | ✅ All passing |
| **Total** | **10** | **100%** |

**Skills demonstrated:**
- RestAssured DSL (`given / when / then`)
- Static `TokenUtil` for OAuth2 token acquisition
- `@Transactional` does NOT propagate over HTTP — test data persists
- External client-side testing (no `@SpringBootTest`, pure black-box)
- `JSONObject` (json-simple) for request body construction
- Robust assertions: `hasItems()` over fragile index-based checks

---

## Key Concepts by Chapter

### Chapter 1 — CRUD & Architecture

```
Controller → Service → Repository → Entity ↔ DTO
```

- Layered REST architecture
- `ResponseEntity<T>` with correct HTTP status codes
- Custom exception handler via `@ControllerAdvice`
- `Pageable` + `Page<T>` for paginated responses
- JPA `@ManyToMany` with join table
- Seeding with `import.sql`

---

### Chapter 2 — Automated Testing

| Annotation | Scope | Description |
|-----------|-------|-------------|
| `@DataJpaTest` | Repository | H2, rollback per test |
| `@ExtendWith(SpringExtension.class)` | Service | Spring beans, no server |
| `@WebMvcTest` | Controller | Web layer only, mocked services |
| `@SpringBootTest` | Integration | Full context |
| `@SpringBootTest + @AutoConfigureMockMvc` | Integration + Web | Full context with HTTP simulation |

**Test naming convention:**
```
methodName_shouldExpectedBehavior_whenCondition
```

**AAA Pattern:**
```java
// Arrange
Product product = ProductFactory.createProduct();
// Act
ProductDTO result = service.findById(existingId);
// Assert
Assertions.assertNotNull(result);
```

**Mockito key patterns:**
```java
when(repository.findById(existingId)).thenReturn(Optional.of(product));
when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
verify(repository, times(1)).deleteById(existingId);
```

---

### Chapter 3 — Validation & Security

**Bean Validation annotations used:**
`@NotBlank` `@NotNull` `@Email` `@Positive` `@PositiveOrZero` `@Size` `@Pattern`

**Custom ConstraintValidator:**
```java
// DB-aware validation (e.g., unique email check)
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
    // queries DB inside isValid() to check uniqueness
}
```

**OAuth2 + JWT flow:**
```
Client → POST /oauth2/token (Basic: clientId:secret + username/password)
      ← JWT (RSA-signed, with username + roles claims)
Client → GET /products (Bearer: <JWT>)
      ← 200 OK or 401/403
```

**Security configuration structure:**
```
SecurityConfig           @Order(1) — dev profile (permissive)
SecurityConfig           @Order(2) — prod profile (restrictive)
AuthorizationServerConfig → issues tokens (RSA)
ResourceServerConfig      → validates tokens, route authorization
```

**Role-based access:**
```java
@PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
@PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.id")
```

---

### Chapter 4 — Query Optimization

**N+1 Problem:**

Occurs when a paginated list loads lazy associations individually per item.
With 10 products each having 2 categories: `1 query (products) + 10 queries (categories) = 11 queries`

**Solution: 2-Query Strategy**
```java
// Step 1: native SQL — paginated IDs (with filters, no JOIN FETCH)
Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);

// Step 2: JPQL JOIN FETCH — full data for those IDs only
List<Product> products = repository.searchProductsWithCategories(productIds);

// Step 3: reorder to match pagination order (Map-based O(n))
List<Product> ordered = Utils.replace(page.getContent(), products);
```

**Generics for reuse:**
```java
public interface IdProjection<ID> {
    ID getId();
}

// Generic replace — works for any entity implementing IdProjection
public static <ID, T extends IdProjection<ID>> List<T> replace(
    List<? extends IdProjection<ID>> ordered,
    List<T> unordered
) { ... }
```

---

### Chapter 5 — JaCoCo Coverage

**Coverage types:**
- **Statement (Line) Coverage** — are these lines executed?
- **Branch Coverage** — are both `if/else` paths tested?
- **Cyclomatic Complexity** — all independent code paths covered?

**White-box vs Black-box:**
| | White-box | Black-box |
|-|-----------|-----------|
| Access | Source code | API/inputs |
| Tests | Unit | Integration, API |
| Tools | JaCoCo | RestAssured, Postman |

**`@Spy` vs `@Mock`:**
```java
@Mock    // completely fake — no real method executes
@Spy     // wraps real object — only overridden methods are faked
// Use @Spy to test a method that calls another method in the same class
```

**Avoiding false positives:**
```java
// FALSE POSITIVE — test passes but nothing is verified
doNothing().when(emailService).sendEmail(any(), any(), any());
service.sendRecoverToken("user@email.com"); // no assertion → always green

// CORRECT — verify the behavior you care about
service.sendRecoverToken("user@email.com");
verify(emailService).sendEmail(eq("user@email.com"), any(), any());
```

---

### Chapter 6 — API Testing: MockMvc vs RestAssured

| | MockMvc | RestAssured |
|-|---------|-------------|
| Server | Simulated (no HTTP) | Real HTTP server |
| Context | `@SpringBootTest` + `@AutoConfigureMockMvc` | External project (no Spring) |
| `@Transactional` | Rolls back after test | **Does NOT propagate via HTTP** |
| Style | Imperative (`.perform().andExpect()`) | DSL (`given().when().then()`) |
| Best for | Integration tests in same app | Black-box API testing |

**RestAssured DSL example:**
```java
given()
    .spec(requestSpec)
    .header("Authorization", "Bearer " + adminToken)
    .contentType(ContentType.JSON)
    .body(requestBody)
.when()
    .post("/movies")
.then()
    .statusCode(201)
    .body("id", notNullValue())
    .body("title", equalTo("The Godfather"));
```

**OAuth2 credentials in tests:**
```
Application credentials → clientId + clientSecret (Basic Auth)
User credentials        → username + password (form body)
```

---

## Architecture Patterns Applied

| Pattern | Applied in |
|---------|-----------|
| Layered Architecture (Controller/Service/Repository) | All projects |
| DTO (Data Transfer Object) | All projects |
| Factory (test data builders) | All test suites |
| Projection (read-only interface for native queries) | DSCatalog, MovieFlix |
| Custom Grant (OAuth2 password flow for Spring Boot 3) | DSCatalog, MovieFlix |
| Strategy (2-query for paginated + filtered lists) | DSCatalog, MovieFlix |
| Generic reuse (IdProjection + Utils.replace) | DSCatalog |
| @ControllerAdvice + @ExceptionHandler (global error) | All projects |

---

## Test Results Summary

| Project | Challenge | Tests | Result |
|---------|-----------|:-----:|:------:|
| event-city | TDD (cap 2) | 7/7 | ✅ 100% |
| empregados | Validation + Security (cap 3) | 12/12 | ✅ 100% |
| movieflix | Queries + Use Cases (cap 4) | All | ✅ 100% |
| dscommerce | JaCoCo coverage (cap 5) | All services | ✅ 100% |
| dsmovie-jacoco | JaCoCo challenge (cap 5) | 16/16 | ✅ 100% |
| dscommerce | MockMvc API tests (cap 6) | All scenarios | ✅ 100% |
| dsmovie | RestAssured challenge (cap 6) | 10/10 | ✅ 100% |

---

## Running a Project

### Requirements

- Java 21+
- Maven 3.8+
- Docker (optional, for PostgreSQL via Compose)

### With H2 (test profile)

```bash
cd catalog
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

Access H2 console: `http://localhost:8080/h2-console`

### With PostgreSQL (dev profile)

```bash
# Start PostgreSQL
docker compose up -d

# Run with dev profile (set env vars or .env file)
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dscatalog
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=yourpassword
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Running Tests

```bash
# Unit + integration tests
./mvnw test

# Generate JaCoCo report
./mvnw test jacoco:report
# Report at: target/site/jacoco/index.html
```

### Obtaining a Token (OAuth2 Password Grant)

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -u "myclientid:myclientsecret" \
  -d "grant_type=password&username=alex@gmail.com&password=123456"
```

---

## Notable Technical Decisions

**Why 2-query strategy instead of a single JOIN FETCH with pagination?**

`JOIN FETCH` with `Pageable` causes Hibernate to load **all** data in memory and paginate in-app (`HHH90003004` warning). With large datasets this causes OOM errors. The 2-query strategy keeps pagination at DB level (native SQL for IDs) and JOIN FETCH only for the current page's items.

**Why RSA for JWT signing instead of HMAC?**

RSA allows decentralized verification: any service with the public key can verify tokens without sharing a secret. Enables multi-service architectures where only the Authorization Server holds the private key.

**Why `doThrow().when()` instead of `when().thenThrow()` for void methods?**

`when().thenThrow()` expects a return value stub — it cannot intercept `void` methods. `doThrow().when(mock).voidMethod()` is the correct Mockito API for void stubbing.

**Why `@Transactional` should NOT be on RestAssured test classes?**

RestAssured sends real HTTP requests. `@Transactional` at the test class only wraps the test method's thread — not the server-side thread that processes the HTTP request. Data committed by the server persists between tests and `@Transactional` rollback does nothing useful. Use `@Sql` or controlled DB seeding instead.

---

## Skills Demonstrated

- **Architecture:** Clean layered REST API design with Spring Boot 3
- **Testing:** Full pyramid — unit → integration → API (black-box)
- **TDD:** Implemented complete features guided only by pre-written tests
- **Security:** OAuth2 Authorization Server + Resource Server from scratch (Spring Boot 3 style)
- **Performance:** Identified and solved N+1 query problem in production-level scenarios
- **Coverage:** Achieved 100% statement + branch coverage with JaCoCo analysis
- **Debugging:** Diagnosed subtle bugs (false positives, `@Transactional` propagation, Jackson deserialization, Circular dependency in Spring context)
