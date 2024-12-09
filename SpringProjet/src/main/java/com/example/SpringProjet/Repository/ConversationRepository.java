package com.example.SpringProjet.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // Recherche une conversation par son nom
    Optional<Conversation> findByName(String name);

    // Recherche les conversations où le dernier message contient un texte spécifique
    List<Conversation> findByLastMessageContaining(String lastMessage);

    // Recherche les conversations où le dernier message a été envoyé après une certaine date
    List<Conversation> findByLastMessageTimeAfter(LocalDateTime dateTime);

    // Supprimer une conversation par son ID (bien que déjà pris en charge par JpaRepository)
    void deleteById(Long id);
}
