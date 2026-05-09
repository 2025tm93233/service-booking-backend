# Service Booking Platform - Microservices Architecture Documentation

## Assignment Submission

**Student Name:** [Your Name]  
**Course:** BITS WILP Fullstack Development  
**Semester:** 2  
**Date:** May 9, 2026  

---

## 1. Overview

The Service Booking Platform is a comprehensive backend system built using Spring Boot that facilitates booking of local services such as plumbing, electrical work, tutoring, cleaning, gardening, and moving services. The application is designed with a modular microservices architecture within a single Spring Boot application, providing RESTful APIs for user management, service listings, booking management, reviews, notifications, and authentication.

### Key Features
- User registration and authentication with role-based access (Customer, Provider, Admin)
- Service catalog management with categories
- Booking system with status tracking
- Review and rating system
- Notification system
- JWT-based security
- MongoDB for data persistence
- Swagger/OpenAPI documentation

---

## 2. Microservices Architecture

Although implemented as a single Spring Boot application for simplicity, the system is architected with clear separation of concerns into distinct microservice modules. Each module handles specific business domains and can be independently developed, tested, and potentially deployed as separate services in a distributed environment.

### 2.1 Service Modules

#### Authentication Service (`/api/auth`)
**Purpose:** Handles user authentication, registration, and session management.

**Key Components:**
- `AuthController`: REST endpoints for login, signup, logout, and current user retrieval
- `AuthService`: Business logic for authentication and user management
- `JwtTokenProvider`: JWT token generation and validation
- `JwtAuthenticationFilter`: Intercepts requests to validate JWT tokens

**Database Interactions:**
- Uses `UserRepository` to query and store user data
- Validates user credentials against MongoDB
- Generates JWT tokens with user information and roles

**Security Integration:**
- Integrates with Spring Security for authentication
- Provides JWT tokens for subsequent API calls

#### User Management Service (`/api/users`)
**Purpose:** Manages user profiles and role-based access control.

**Key Components:**
- `UserController`: CRUD operations for user management
- `UserService`: Business logic for user operations
- `CustomUserDetailsService`: Implements Spring Security's UserDetailsService

**Database Interactions:**
- `UserRepository` extends MongoRepository for MongoDB operations
- Supports queries by email, role, and ID
- Handles user creation, updates, and deletion

#### Category Service (`/api/categories`)
**Purpose:** Manages service categories for organization.

**Key Components:**
- `CategoryController`: REST endpoints for category management
- `CategoryService`: Business logic for category operations

**Database Interactions:**
- `CategoryRepository` for MongoDB CRUD operations
- Supports slug-based lookups for SEO-friendly URLs

#### Service Management Service (`/api/services`)
**Purpose:** Manages the catalog of available services offered by providers.

**Key Components:**
- `ServiceController`: REST endpoints for service listings and management
- `ServiceService`: Business logic for service operations

**Database Interactions:**
- `ServiceRepository` for storing and retrieving service information
- Supports pagination for large service catalogs
- Links services to providers and categories

#### Booking Service (`/api/bookings`)
**Purpose:** Handles the core booking functionality between customers and service providers.

**Key Components:**
- `BookingController`: REST endpoints for booking operations
- `BookingService`: Complex business logic for booking management

**Database Interactions:**
- `BookingRepository` for booking data persistence
- Interacts with `UserRepository` and `ServiceRepository` for data validation
- Supports status updates and booking history

**Inter-Service Communication:**
- Retrieves customer and provider details from User Service
- Fetches service details from Service Management Service
- Updates booking status and notifies relevant parties

#### Review Service (`/api/reviews`)
**Purpose:** Manages customer reviews and ratings for services.

**Key Components:**
- `ReviewController`: REST endpoints for review management
- `ReviewService`: Business logic for review operations

**Database Interactions:**
- `ReviewRepository` for storing review data
- Calculates average ratings for services
- Supports filtering reviews by service or user

#### Notification Service (`/api/notifications`)
**Purpose:** Handles user notifications for booking updates and system messages.

**Key Components:**
- `NotificationController`: REST endpoints for notification management
- `NotificationService`: Business logic for notification operations

**Database Interactions:**
- `NotificationRepository` for storing notification data
- Tracks read/unread status
- Supports bulk operations for marking notifications as read

---

## 3. Inter-Service Communication

### 3.1 Synchronous Communication

The services communicate synchronously through direct method calls within the same application context:

1. **Booking Service ↔ User Service & Service Management Service**
   - When creating a booking, `BookingService` calls `UserRepository.findByEmail()` to get customer details
   - Calls `ServiceRepository.findById()` to validate service existence and get provider information

2. **Authentication Service ↔ User Service**
   - `AuthService` uses `UserRepository` for user validation and registration
   - `JwtAuthenticationFilter` uses `CustomUserDetailsService` which queries `UserRepository`

3. **Review Service ↔ Service Management Service**
   - Calculates average ratings by querying reviews for specific services

### 3.2 Data Flow Patterns

#### User Registration Flow:
1. Client sends signup request to `/api/auth/signup`
2. `AuthController` delegates to `AuthService.signup()`
3. `AuthService` validates email uniqueness via `UserRepository`
4. Creates new `User` entity and saves via `UserRepository`
5. Generates JWT token using `JwtTokenProvider`
6. Returns `AuthResponse` with user data and token

#### Booking Creation Flow:
1. Authenticated client sends booking request to `/api/bookings`
2. `BookingController` delegates to `BookingService.createBooking()`
3. `BookingService` retrieves customer from `UserRepository`
4. Retrieves service details from `ServiceRepository`
5. Validates business rules (service availability, user roles)
6. Creates `Booking` entity and saves via `BookingRepository`
7. Returns booking confirmation

---

## 4. Database Layer

### 4.1 Database Technology
- **MongoDB**: NoSQL document database for flexible data storage
- **Spring Data MongoDB**: Provides repository abstraction and query methods

### 4.2 Repository Pattern Implementation

Each service module has dedicated repositories extending `MongoRepository`:

```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
}
```

### 4.3 Data Models

#### Core Entities:
- **User**: Stores user information, roles, and authentication data
- **Service**: Service listings with pricing, descriptions, and provider associations
- **Booking**: Booking records with status tracking
- **Category**: Service categorization
- **Review**: Customer feedback and ratings
- **Notification**: User notifications

### 4.4 Database Configuration

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=service-booking
```

### 4.5 Data Relationships

- **User-Service**: One-to-Many (Provider can offer multiple services)
- **Service-Booking**: One-to-Many (Service can have multiple bookings)
- **User-Booking**: One-to-Many (Customer/Provider can have multiple bookings)
- **Service-Review**: One-to-Many (Service can have multiple reviews)
- **User-Review**: One-to-Many (User can write multiple reviews)

---

## 5. Security Layer

### 5.1 Authentication & Authorization

#### JWT-Based Authentication:
- **Token Generation**: `JwtTokenProvider.generateToken()` creates signed JWT tokens
- **Token Validation**: `JwtTokenProvider.validateToken()` verifies token integrity
- **Token Extraction**: `JwtTokenProvider.extractUsername()` retrieves user identity

#### Security Configuration (`SecurityConfig`):
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**", "/api/services/**", "/api/categories/**").permitAll()
    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
    .anyRequest().authenticated()
)
```

### 5.2 Request Processing Flow

1. **Incoming Request**: HTTP request reaches the application
2. **JWT Filter**: `JwtAuthenticationFilter` intercepts the request
3. **Token Validation**: Extracts and validates JWT from Authorization header
4. **User Loading**: `CustomUserDetailsService.loadUserByUsername()` retrieves user details
5. **Security Context**: Sets authentication in `SecurityContextHolder`
6. **Authorization**: `SecurityConfig` checks request matchers
7. **Controller Execution**: Authenticated request reaches the controller

### 5.3 Role-Based Access Control

- **User Roles**: CUSTOMER, PROVIDER, ADMIN
- **Method-Level Security**: `@PreAuthorize` annotations for fine-grained access control
- **Endpoint Protection**: Different endpoints require different authentication levels

### 5.4 Security Features

- **Password Encryption**: BCrypt hashing for password storage
- **CORS Configuration**: Configured for frontend integration
- **CSRF Protection**: Disabled for API-first design
- **Session Management**: Stateless with JWT tokens

---

## 6. API Design & Documentation

### 6.1 RESTful API Design

- **Resource-Based URLs**: `/api/{resource}/{id}`
- **HTTP Methods**: GET, POST, PUT, DELETE for CRUD operations
- **Consistent Response Format**: `ApiResponse<T>` wrapper for all responses
- **HTTP Status Codes**: Standard codes (200, 201, 400, 401, 404, 500)

### 6.2 Swagger/OpenAPI Integration

- **Configuration**: `SwaggerConfig` defines API metadata and security schemes
- **Annotations**: Controllers use `@Operation`, `@ApiResponses` for documentation
- **UI Access**: Available at `/swagger-ui/index.html`
- **API Specs**: OpenAPI 3.0 specification at `/v3/api-docs`

### 6.3 Error Handling

- **Global Exception Handler**: `GlobalExceptionHandler` for centralized error management
- **Custom Exceptions**: Domain-specific error handling
- **Consistent Error Responses**: Standardized error format across all endpoints

---

## 7. Technology Stack

### Backend Framework
- **Spring Boot 3.2.0**: Main application framework
- **Spring Web**: REST API development
- **Spring Data MongoDB**: Database abstraction
- **Spring Security**: Authentication and authorization

### Security & Authentication
- **JWT (JSON Web Token)**: Stateless authentication
- **Spring Security**: Comprehensive security framework
- **BCrypt**: Password hashing

### Database & Persistence
- **MongoDB**: NoSQL document database
- **Spring Data MongoDB**: Repository pattern implementation

### Development Tools
- **Maven**: Dependency management and build tool
- **Lombok**: Code generation for boilerplate reduction
- **Springdoc OpenAPI**: API documentation

### Testing & Quality
- **JUnit**: Unit testing framework
- **Spring Boot Test**: Integration testing support

---

## 8. Deployment & Configuration

### Application Configuration
- **application.properties**: Environment-specific settings
- **Profile Management**: Support for dev, test, prod environments
- **Externalized Configuration**: Database URLs, secrets, CORS settings

### Build & Deployment
- **Maven Build**: `mvn clean install` for JAR creation
- **Executable JAR**: Self-contained application package
- **Port Configuration**: Default port 8080, configurable

### Production Considerations
- **Security Hardening**: Change default JWT secrets
- **Database Configuration**: Use production MongoDB instances
- **Logging**: Configure appropriate log levels
- **Monitoring**: Add health checks and metrics

---

## 9. Conclusion

The Service Booking Platform demonstrates a well-structured microservices architecture within a Spring Boot monolith, providing clear separation of concerns, robust security, and scalable database interactions. The modular design allows for independent development and testing of each service module while maintaining cohesive functionality through well-defined interfaces and data flows.

The implementation showcases industry best practices in:
- RESTful API design
- JWT-based authentication
- MongoDB integration with Spring Data
- Comprehensive security configuration
- API documentation with OpenAPI/Swagger

This architecture provides a solid foundation for a service booking platform and can be easily extended or refactored into truly distributed microservices as the application scales.

---

**End of Documentation**</content>
<parameter name="filePath">C:\BITS WILP\Semester 2\Fullstack\service-booking-backend\Assignment_Documentation.md
