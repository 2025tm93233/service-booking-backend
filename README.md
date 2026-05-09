# Service Booking Backend API

A Spring Boot microservices application with MongoDB for the Service Booking Platform.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MongoDB 4.4 or higher

## Getting Started

### 1. Install MongoDB

Make sure MongoDB is running on your system:

```bash
# On Windows
# Start MongoDB service from Services or run:
mongod --dbpath "C:\data\db"

# On Linux/Mac
sudo systemctl start mongod
```

### 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=service-booking

# JWT Configuration
jwt.secret=your-secret-key-change-this-in-production
jwt.expiration=86400000

# CORS Configuration
cors.allowed-origins=http://localhost:5173
```

### 3. Build and Run

```bash
# Navigate to project directory
cd service-booking-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

## API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `GET /api/auth/me` - Get current user
- `POST /api/auth/logout` - User logout

### Users

- `GET /api/users` - Get all users (requires authentication)
- `GET /api/users/{id}` - Get user by ID (requires authentication)
- `GET /api/users/role/{role}` - Get users by role (CUSTOMER/PROVIDER/ADMIN) (requires authentication)
- `POST /api/users` - Create new user (requires authentication)
- `PUT /api/users/{id}` - Update user (requires authentication)
- `DELETE /api/users/{id}` - Delete user (requires authentication)

### Categories

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/categories/slug/{slug}` - Get category by slug
- `POST /api/categories` - Create new category (requires authentication)
- `PUT /api/categories/{id}` - Update category (requires authentication)
- `DELETE /api/categories/{id}` - Delete category (requires authentication)

### Services

- `GET /api/services` - Get all services (with pagination)
- `GET /api/services/{id}` - Get service by ID
- `GET /api/services/provider/{providerId}` - Get services by provider
- `POST /api/services` - Create new service (requires authentication)
- `PUT /api/services/{id}` - Update service (requires authentication)
- `DELETE /api/services/{id}` - Delete service (requires authentication)

### Bookings

- `GET /api/bookings/customer` - Get customer bookings (requires authentication)
- `GET /api/bookings/provider` - Get provider bookings (requires authentication)
- `GET /api/bookings/{id}` - Get booking by ID
- `POST /api/bookings` - Create booking (requires authentication)
- `PATCH /api/bookings/{id}/status` - Update booking status (requires authentication)
- `POST /api/bookings/{id}/cancel` - Cancel booking (requires authentication)

### Reviews

- `GET /api/reviews` - Get all reviews
- `GET /api/reviews/service/{serviceId}` - Get reviews for a service
- `GET /api/reviews/user/{userId}` - Get reviews by user
- `GET /api/reviews/service/{serviceId}/average` - Get average rating for a service
- `POST /api/reviews` - Create review (requires authentication)
- `PUT /api/reviews/{id}` - Update review (requires authentication)
- `DELETE /api/reviews/{id}` - Delete review (requires authentication)

### Notifications

- `GET /api/notifications` - Get user notifications (requires authentication)
- `GET /api/notifications/unread` - Get unread notifications (requires authentication)
- `GET /api/notifications/count` - Get unread count (requires authentication)
- `POST /api/notifications/{id}/read` - Mark notification as read (requires authentication)
- `POST /api/notifications/read-all` - Mark all notifications as read (requires authentication)
- `DELETE /api/notifications/{id}` - Delete notification (requires authentication)

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Project Structure

```
service-booking-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/servicebooking/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── dto/             # Data transfer objects
│   │   │       ├── exception/       # Exception handlers
│   │   │       ├── model/           # Domain models
│   │   │       ├── repository/      # MongoDB repositories
│   │   │       ├── security/        # Security configuration
│   │   │       └── service/         # Business logic
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Technologies Used

- Spring Boot 3.2.0
- Spring Data MongoDB
- Spring Security
- JWT (JSON Web Tokens)
- Lombok
- Maven

## Development

### Running Tests

```bash
mvn test
```

### Code Style

The project uses Lombok to reduce boilerplate code.

## Production Deployment

Before deploying to production:

1. Change the JWT secret key in `application.properties`
2. Configure MongoDB connection string for production
3. Enable HTTPS
4. Set up proper CORS configuration
5. Configure logging levels

## License

This project is licensed under the MIT License.
