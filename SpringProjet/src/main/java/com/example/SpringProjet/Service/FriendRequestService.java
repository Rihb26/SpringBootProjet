package com.example.SpringProjet.Service;

import com.example.SpringProjet.Repository.FriendRequest;
import com.example.SpringProjet.Repository.FriendRequestRepository;
import com.example.SpringProjet.Repository.Notification;
import com.example.SpringProjet.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, NotificationRepository notificationRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationRepository = notificationRepository;
    }

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Les IDs de l'expéditeur et du destinataire ne peuvent pas être nuls.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas s'envoyer une demande d'ami à lui-même.");
        }

        return friendRequestRepository.findBySenderIdAndReceiverIdAndStatus(senderId, receiverId, "PENDING")
                .orElseGet(() -> {
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setSenderId(senderId);
                    friendRequest.setReceiverId(receiverId);
                    friendRequest.setStatus("PENDING");
                    friendRequest.setCreatedAt(LocalDateTime.now());

                    FriendRequest savedRequest = friendRequestRepository.save(friendRequest);

                    // Ajoutez ces logs pour déboguer
                    System.out.println("Création de la notification pour l'utilisateur : " + receiverId);
                    Notification notification = new Notification();
                    notification.setUserId(receiverId);
                    notification.setMessage("Vous avez reçu une nouvelle demande d'ami de l'utilisateur " + senderId);
                    notification.setCreatedAt(LocalDateTime.now());

                    notificationRepository.save(notification);
                    System.out.println("Notification enregistrée avec succès.");

                    return savedRequest;
                });
    }


    public List<FriendRequest> getSentRequests(Long senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("L'ID de l'expéditeur ne peut pas être nul.");
        }
        return friendRequestRepository.findBySenderId(senderId);
    }

    public List<FriendRequest> getReceivedRequests(Long receiverId) {
        if (receiverId == null) {
            throw new IllegalArgumentException("L'ID du destinataire ne peut pas être nul.");
        }
        return friendRequestRepository.findByReceiverId(receiverId);
    }

    public FriendRequest acceptFriendRequest(Long requestId) {
        // Récupérer la demande d'ami par son ID
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("La demande d'ami n'existe pas."));

        // Vérifier que la demande est toujours en attente
        if (!friendRequest.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("La demande d'ami n'est pas en attente.");
        }

        // Mettre à jour le statut de la demande à ACCEPTED
        friendRequest.setStatus("ACCEPTED");
        friendRequestRepository.save(friendRequest);

        // Créer une notification pour l'expéditeur
        Notification notification = new Notification();
        notification.setUserId(friendRequest.getSenderId());
        notification.setMessage("Votre demande d'ami a été acceptée par l'utilisateur " + friendRequest.getReceiverId());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);

        return friendRequest;
    }

}
