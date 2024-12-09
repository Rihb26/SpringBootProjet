package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Repository.Notification;
import com.example.SpringProjet.Repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur via un endpoint explicite.
     *
     * @param userId ID de l'utilisateur pour lequel récupérer les notifications non lues.
     * @return Liste des notifications non lues.
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marquer toutes les notifications comme lues pour un utilisateur.
     *
     * @param userId ID de l'utilisateur pour lequel marquer les notifications comme lues.
     * @return Message confirmant l'opération.
     */
    @PatchMapping("/mark-read")
    public ResponseEntity<String> markNotificationsAsRead(@RequestParam Long userId) {

        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);


        notifications.forEach(notification -> notification.setRead(true));


        notificationRepository.saveAll(notifications);

        return ResponseEntity.ok("Toutes les notifications ont été marquées comme lues.");
    }

    /**
     * Créer une notification personnalisée pour un utilisateur.
     *
     * @param userId  ID de l'utilisateur recevant la notification.
     * @param message Contenu de la notification.
     * @return La notification créée.
     */
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long userId,
            @RequestParam String message
    ) {

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(java.time.LocalDateTime.now());


        Notification savedNotification = notificationRepository.save(notification);

        return ResponseEntity.ok(savedNotification);
    }
}
