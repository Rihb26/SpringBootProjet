package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Repository.FriendRequest;
import com.example.SpringProjet.Service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping
    public ResponseEntity<FriendRequest> sendFriendRequest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            // Appel du service pour créer la demande
            FriendRequest friendRequest = friendRequestService.sendFriendRequest(senderId, receiverId);
            return ResponseEntity.ok(friendRequest);
        } catch (Exception e) {
            // Log pour débogage
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequest>> getSentRequests(@RequestParam Long senderId) {
        try {
            if (senderId == null) {
                return ResponseEntity.badRequest().body(null);
            }
            List<FriendRequest> requests = friendRequestService.getSentRequests(senderId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getReceivedRequests(@RequestParam Long receiverId) {
        try {
            if (receiverId == null) {
                return ResponseEntity.badRequest().body("L'ID du destinataire est requis.");
            }

            List<FriendRequest> requests = friendRequestService.getReceivedRequests(receiverId);

            if (requests.isEmpty()) {
                return ResponseEntity.ok("Aucune demande d'ami reçue.");
            }

            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<?> acceptFriendRequest(
            @PathVariable Long id,
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            FriendRequest acceptedRequest = friendRequestService.acceptFriendRequest(id, senderId, receiverId);
            return ResponseEntity.ok(acceptedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }


    @DeleteMapping("/{id}/decline")
    public ResponseEntity<String> declineFriendRequest(@PathVariable Long id) {
        friendRequestService.declineFriendRequest(id);
        return ResponseEntity.ok("Friend request declined successfully");
    }


    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<String> cancelFriendRequest(@PathVariable Long id) {
        friendRequestService.cancelFriendRequest(id);
        return ResponseEntity.ok("Friend request canceled successfully");
    }

}
