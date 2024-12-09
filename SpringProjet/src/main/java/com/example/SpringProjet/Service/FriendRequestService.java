package com.example.SpringProjet.Service;

import com.example.SpringProjet.Event.FriendRequestAcceptedEvent;
import com.example.SpringProjet.Event.FriendRequestReceivedEvent;
import com.example.SpringProjet.Repository.FriendRequest;
import com.example.SpringProjet.Repository.FriendRequestRepository;
import com.example.SpringProjet.Repository.Notification;
import com.example.SpringProjet.Repository.NotificationRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;


    public FriendRequestService(FriendRequestRepository friendRequestRepository, NotificationRepository notificationRepository,ApplicationEventPublisher eventPublisher) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
    }

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Les IDs de l'expéditeur et du destinataire ne peuvent pas être nuls.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas s'envoyer une demande d'ami à lui-même.");
        }
        eventPublisher.publishEvent(new FriendRequestReceivedEvent(this, receiverId, senderId));
        return friendRequestRepository.findBySenderIdAndReceiverIdAndStatus(senderId, receiverId, "PENDING")
                .orElseGet(() -> {
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setSenderId(senderId);
                    friendRequest.setReceiverId(receiverId);
                    friendRequest.setStatus("PENDING");
                    friendRequest.setCreatedAt(LocalDateTime.now());

                    FriendRequest savedRequest = friendRequestRepository.save(friendRequest);


                    System.out.println("Création de la notification pour l'utilisateur : " + receiverId);

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

    public FriendRequest acceptFriendRequest(Long requestId,Long senderId, Long receiverId) {

        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("La demande d'ami n'existe pas."));


        if (!friendRequest.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("La demande d'ami n'est pas en attente.");
        }


        friendRequest.setStatus("ACCEPTED");
        friendRequestRepository.save(friendRequest);

        eventPublisher.publishEvent(new FriendRequestAcceptedEvent(this, receiverId, senderId));


        Notification notification = new Notification();
        notification.setUserId(friendRequest.getSenderId());
        notification.setMessage("Votre demande d'ami a été acceptée par l'utilisateur " + friendRequest.getReceiverId());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);

        return friendRequest;
    }

    public void declineFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with ID: " + requestId));

        friendRequest.setStatus("DECLINED");
        friendRequestRepository.save(friendRequest);
    }

    public void cancelFriendRequest(Long id) {

        FriendRequest friendRequest = friendRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friend request not found with ID: " + id));


        friendRequestRepository.deleteById(id);
    }

}
