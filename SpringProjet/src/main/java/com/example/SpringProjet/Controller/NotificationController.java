package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Dto.NotificationSnapshot;
import com.example.SpringProjet.Service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationSnapshot> createNotification(
            @RequestParam Long userId,
            @RequestParam String message) {
        NotificationSnapshot snapshot = notificationService.createNotification(userId, message);
        return ResponseEntity.ok(snapshot);
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationSnapshot>> getUnreadNotifications(@PathVariable Long userId) {
        List<NotificationSnapshot> snapshots = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(snapshots);
    }

    @PostMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok("Notification marquée comme lue.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
