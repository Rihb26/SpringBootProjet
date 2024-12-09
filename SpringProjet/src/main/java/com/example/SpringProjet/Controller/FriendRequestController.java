package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Dto.FriendRequestSnapshot;
import com.example.SpringProjet.Service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping
    public FriendRequestSnapshot sendFriendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return friendRequestService.sendFriendRequest(senderId, receiverId);
    }

    @GetMapping("/sent")
    public List<FriendRequestSnapshot> getSentRequests(@RequestParam Long senderId) {
        return friendRequestService.getSentRequests(senderId);
    }

    @GetMapping("/received")
    public List<FriendRequestSnapshot> getReceivedRequests(@RequestParam Long receiverId) {
        return friendRequestService.getReceivedRequests(receiverId);
    }

    @PutMapping("/{id}/accept")
    public FriendRequestSnapshot acceptFriendRequest(@PathVariable Long id) {
        return friendRequestService.acceptFriendRequest(id);
    }
}
