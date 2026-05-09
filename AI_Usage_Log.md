# AI Usage Log - Service Booking Backend Development

## AI Prompts Used for Debugging and Issue Resolution

### 1. Swagger UI Access Issue
**Context:** After implementing Swagger configuration, the Swagger UI was not accessible at localhost:8080/swagger, returning a 404 error.

**Prompt Used:**
```
I'm working on a Spring Boot application with springdoc-openapi for API documentation. My SwaggerConfig is set up correctly, and the dependency is included. However, localhost:8080/swagger returns 404. The default path should be /swagger-ui/index.html, but I want /swagger to redirect to it. How can I implement this redirect?
```

**AI Response Summary:**
- Confirmed that springdoc-openapi uses /swagger-ui/index.html as default
- Suggested creating a controller with @GetMapping("/swagger") that returns "redirect:/swagger-ui/index.html"
- Advised updating SecurityConfig to permit access to "/swagger" endpoint

**Implementation:**
- Created SwaggerRedirectController.java
- Updated SecurityConfig.java to include "/swagger" in permitAll list
- Tested and confirmed redirect works

### 2. Security Configuration Debugging
**Context:** After adding the redirect controller, I needed to ensure it wasn't blocked by Spring Security.

**Prompt Used:**
```
In my Spring Boot app with Spring Security, I have permitAll for certain endpoints. I added a new controller for /swagger redirect, but I'm getting 401 unauthorized. How do I add /swagger to the permitted endpoints in SecurityConfig?
```

**AI Response Summary:**
- Explained how to modify the authorizeHttpRequests chain
- Showed syntax for adding multiple requestMatchers
- Confirmed the pattern for permitAll configuration

**Implementation:**
- Updated SecurityConfig.java to include "/swagger" in the permitAll requestMatchers
- Verified the change resolved the authorization issue

### 3. MongoDB Connection Issues
**Context:** Application was failing to start due to MongoDB connection problems.

**Prompt Used:**
```
My Spring Boot application is failing to connect to MongoDB. I'm getting connection refused errors. The application.properties has:
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=service-booking

How can I troubleshoot MongoDB connection issues?
```

**AI Response Summary:**
- Suggested checking if MongoDB service is running
- Recommended verifying port availability
- Advised checking MongoDB logs
- Provided alternative connection string format

**Resolution:**
- Confirmed MongoDB was not running
- Started MongoDB service using system commands
- Application started successfully

### 4. Dependency Version Conflicts
**Context:** Maven build was failing due to version conflicts between Spring Boot and springdoc-openapi.

**Prompt Used:**
```
I'm getting Maven dependency resolution errors with springdoc-openapi-starter-webmvc-ui version 2.3.0 and Spring Boot 3.2.0. The error suggests version incompatibility. What version of springdoc should I use with Spring Boot 3.2.0?
```

**AI Response Summary:**
- Confirmed compatibility between springdoc 2.3.0 and Spring Boot 3.2.0
- Suggested checking for conflicting dependencies
- Recommended using dependency tree analysis: mvn dependency:tree
- Advised clearing Maven cache if needed

**Resolution:**
- Ran mvn dependency:tree to identify conflicts
- Found conflicting Jackson versions
- Updated pom.xml to exclude conflicting Jackson dependencies
- Build succeeded

### 5. JWT Token Validation Issues
**Context:** Authentication was failing even with valid JWT tokens.

**Prompt Used:**
```
My JWT authentication filter is not validating tokens correctly. The filter extracts the token from Authorization header, but validateToken() is returning false. The JwtTokenProvider uses io.jsonwebtoken library. What could be causing token validation to fail?
```

**AI Response Summary:**
- Suggested checking token signing key consistency
- Recommended verifying token expiration
- Advised debugging token extraction and parsing
- Provided code snippets for validation debugging

**Resolution:**
- Added logging to JwtTokenProvider to debug token parsing
- Discovered signing key mismatch between generation and validation
- Fixed key configuration in application.properties
- Authentication working correctly

### 6. CORS Configuration Problems 
**Context:** Frontend requests were being blocked by CORS policy.

**Prompt Used:**
```
My Spring Boot backend has CORS configured for http://localhost:5173, but frontend requests are still being blocked. The SecurityConfig has cors configuration. What might be wrong with my CORS setup?
```

**AI Response Summary:**
- Suggested checking if CORS is applied to all endpoints
- Recommended verifying allowed methods and headers
- Advised checking browser console for specific CORS errors
- Provided updated CORS configuration example

**Resolution:**
- Updated CorsConfigurationSource to include OPTIONS method
- Added allowCredentials(true) for cookie support
- CORS errors resolved

### 7. Repository Query Issues
**Context:** Custom repository methods were not working as expected.

**Prompt Used:**
```
My MongoRepository custom query methods are not finding data. For example, findByEmail() is returning empty Optional even when the document exists. How do I debug Spring Data MongoDB queries?
```

**AI Response Summary:**
- Suggested enabling MongoDB query logging
- Recommended checking document structure in database
- Advised using MongoDB compass to verify data
- Provided logging configuration for debugging

**Resolution:**
- Added logging.level.org.springframework.data.mongodb=DEBUG
- Discovered case sensitivity issues in queries
- Fixed query methods to handle case-insensitive searches
- Queries working correctly

### 8. Build and Compilation Errors
**Context:** Maven compile was failing with annotation processing errors.

**Prompt Used:**
```
Maven compilation is failing with errors related to Lombok annotations. I'm using @Data, @Builder, etc., but getting "cannot find symbol" errors. How do I fix Lombok compilation issues in Spring Boot?
```

**AI Response Summary:**
- Suggested checking Lombok version compatibility
- Recommended IDE Lombok plugin installation
- Advised Maven compiler plugin configuration
- Provided annotation processor configuration

**Resolution:**
- Updated Lombok version to match Spring Boot
- Added lombok.config file for annotation processor
- Compilation successful

### 9. API Response Format Issues
**Context:** API responses were inconsistent across endpoints.

**Prompt Used:**
```
I want to standardize API responses across my Spring Boot controllers. Currently some return raw objects, others use ResponseEntity. How can I create a consistent response wrapper for all endpoints?
```

**AI Response Summary:**
- Suggested creating ApiResponse<T> generic wrapper class
- Recommended using ResponseEntity<ApiResponse<T>>
- Provided example implementation with success/error methods
- Advised updating all controllers to use the wrapper

**Resolution:**
- Created ApiResponse.java DTO class
- Updated all controllers to use consistent response format
- Improved API consistency

### 10. Exception Handling Standardization
**Context:** Exceptions were not being handled consistently.

**Prompt Used:**
```
My Spring Boot controllers are throwing various RuntimeExceptions. I need a global exception handler to standardize error responses. How do I implement @ControllerAdvice for consistent error handling?
```

**AI Response Summary:**
- Explained @ControllerAdvice and @ExceptionHandler usage
- Suggested creating GlobalExceptionHandler class
- Provided examples for different exception types
- Recommended custom exception classes for domain errors

**Resolution:**
- Created GlobalExceptionHandler.java
- Implemented handlers for common exceptions
- Added custom exceptions for business logic errors
- Error responses now consistent

---

## Summary of AI Usage

**Total Prompts Used:** 10  
**Primary Focus:** Debugging and issue resolution  
**AI Role:** Problem-solving assistant, not code generator  

The AI was used exclusively for:
- Troubleshooting configuration issues
- Debugging runtime problems
- Resolving dependency conflicts
- Understanding framework-specific solutions
- Learning best practices for error handling

All code implementations were done manually based on AI guidance, ensuring academic integrity while leveraging AI for efficient problem-solving.

---
