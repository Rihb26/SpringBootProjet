package com.example.SpringProjet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, String status);

    List<FriendRequest> findBySenderId(Long senderId);

    List<FriendRequest> findByReceiverId(Long receiverId);
}
