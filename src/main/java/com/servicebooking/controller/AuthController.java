package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.dto.AuthResponse;
import com.servicebooking.dto.LoginRequest;
import com.servicebooking.dto.SignupRequest;
import com.servicebooking.model.User;
import com.servicebooking.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "User login", description = "Authenticate user with email and password and return JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @Operation(summary = "User registration", description = "Register a new user with email, password, and role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        try {
            AuthResponse response = authService.signup(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Signup successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @Operation(summary = "Get current user", description = "Get details of the currently authenticated user")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User details retrieved", content = @Content(schema = @Schema(implementation = User.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(Authentication authentication) {
        try {
            User user = authService.getCurrentUser(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<User>error(e.getMessage()));
        }
    }

    @Operation(summary = "User logout", description = "Logout the current user")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully"));
    }
}
