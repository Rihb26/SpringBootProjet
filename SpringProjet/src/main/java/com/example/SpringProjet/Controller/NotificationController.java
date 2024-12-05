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
     * Récupérer les notifications non lues d'un utilisateur.
     *
     * @param userId ID de l'utilisateur pour lequel récupérer les notifications.
     * @return Liste des notifications non lues.
     */
    @GetMapping
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
        // Récupérer les notifications non lues
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);

        // Marquer chaque notification comme lue
        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        // Sauvegarder les modifications
        notificationRepository.saveAll(notifications);

        return ResponseEntity.ok("Toutes les notifications ont été marquées comme lues.");
    }
}

