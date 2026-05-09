package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.model.Notification;
import com.servicebooking.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Notification Management", description = "APIs for managing user notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get user notifications", description = "Retrieves all notifications for the authenticated user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications(Authentication authentication) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Notification>>error(e.getMessage()));
        }
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications", description = "Retrieves unread notifications for the authenticated user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread notifications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(Authentication authentication) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Notification>>error(e.getMessage()));
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get unread notification count", description = "Retrieves the count of unread notifications for the authenticated user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread count retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication authentication) {
        try {
            long count = notificationService.getUnreadCount(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Long>error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @Parameter(description = "Notification ID") @PathVariable String id,
            Authentication authentication) {
        try {
            Notification notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success(notification));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Notification>error(e.getMessage()));
        }
    }

    @PostMapping("/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications for the authenticated user as read")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All notifications marked as read successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        try {
            notificationService.markAllAsRead(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Deletes a specific notification")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request or error occurred")
    })
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable String id,
            Authentication authentication) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
