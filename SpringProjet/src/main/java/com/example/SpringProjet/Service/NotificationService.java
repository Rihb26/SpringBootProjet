package com.example.SpringProjet.Service;

import com.example.SpringProjet.Dto.NotificationSnapshot;
import com.example.SpringProjet.Repository.Notification;
import com.example.SpringProjet.Repository.NotificationRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationSnapshot createNotification(Long userId, String message) {
        Notification notification = new Notification(userId, message);
        Notification savedNotification = notificationRepository.save(notification);
        return savedNotification.toSnapshot();
    }

    public List<NotificationSnapshot> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
                .stream()
                .map(Notification::toSnapshot)
                .collect(Collectors.toList());
    }

    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification introuvable."));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
