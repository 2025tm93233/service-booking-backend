package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.model.Notification;
import com.servicebooking.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications(Authentication authentication) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Notification>>error(e.getMessage()));
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(Authentication authentication) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Notification>>error(e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication authentication) {
        try {
            long count = notificationService.getUnreadCount(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Long>error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @PathVariable String id,
            Authentication authentication) {
        try {
            Notification notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success(notification));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Notification>error(e.getMessage()));
        }
    }

    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        try {
            notificationService.markAllAsRead(authentication.getName());
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable String id,
            Authentication authentication) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}
