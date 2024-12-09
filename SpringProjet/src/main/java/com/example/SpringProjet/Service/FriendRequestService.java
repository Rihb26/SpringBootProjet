package com.example.SpringProjet.Service;

import com.example.SpringProjet.Dto.FriendRequestSnapshot;
import com.example.SpringProjet.Repository.FriendRequest;
import com.example.SpringProjet.Repository.FriendRequestRepository;
import com.example.SpringProjet.Repository.Notification;
import com.example.SpringProjet.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, NotificationRepository notificationRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationRepository = notificationRepository;
    }

    public FriendRequestSnapshot sendFriendRequest(Long senderId, Long receiverId) {
        validateSenderAndReceiver(senderId, receiverId);

        // Vérifie si une demande en attente existe déjà
        return friendRequestRepository.findBySenderIdAndReceiverIdAndStatus(senderId, receiverId, "PENDING")
                .map(FriendRequest::toSnapshot)
                .orElseGet(() -> {
                    // Création de la demande d'ami
                    FriendRequest friendRequest = new FriendRequest(senderId, receiverId);
                    FriendRequest savedRequest = friendRequestRepository.save(friendRequest);

                    // Création de la notification pour le destinataire
                    Notification notification = new Notification(receiverId,
                            "Vous avez reçu une nouvelle demande d'ami de l'utilisateur " + senderId);
                    notificationRepository.save(notification);

                    return savedRequest.toSnapshot();
                });
    }

    public List<FriendRequestSnapshot> getSentRequests(Long senderId) {
        validateUserId(senderId, "L'ID de l'expéditeur ne peut pas être nul.");
        return friendRequestRepository.findBySenderId(senderId)
                .stream()
                .map(FriendRequest::toSnapshot)
                .collect(Collectors.toList());
    }

    public List<FriendRequestSnapshot> getReceivedRequests(Long receiverId) {
        validateUserId(receiverId, "L'ID du destinataire ne peut pas être nul.");
        return friendRequestRepository.findByReceiverId(receiverId)
                .stream()
                .map(FriendRequest::toSnapshot)
                .collect(Collectors.toList());
    }

    public FriendRequestSnapshot acceptFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("La demande d'ami n'existe pas."));

        if (!"PENDING".equals(friendRequest.toSnapshot().status())) {
            throw new IllegalArgumentException("Cette demande d'ami n'est pas en attente.");
        }

        // Accepter la demande
        friendRequest.accept();
        FriendRequest updatedRequest = friendRequestRepository.save(friendRequest);

        // Notification pour l'expéditeur
        Notification notification = new Notification(updatedRequest.toSnapshot().senderId(),
                "Votre demande d'ami a été acceptée par l'utilisateur " + updatedRequest.toSnapshot().receiverId());
        notificationRepository.save(notification);

        return updatedRequest.toSnapshot();
    }

    private void validateSenderAndReceiver(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Les IDs de l'expéditeur et du destinataire ne peuvent pas être nuls.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas s'envoyer une demande d'ami à lui-même.");
        }
    }

    private void validateUserId(Long userId, String errorMessage) {
        if (userId == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
