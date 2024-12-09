package com.example.SpringProjet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Récupère tous les fichiers associés à une conversation.
     *
     * @param conversationId ID de la conversation.
     * @return Liste des fichiers de la conversation.
     */
    List<File> findByConversationId(Long conversationId);

    /**
     * Récupère tous les fichiers envoyés par un utilisateur dans une conversation spécifique.
     *
     * @param conversationId ID de la conversation.
     * @param senderId       ID de l'utilisateur qui a envoyé les fichiers.
     * @return Liste des fichiers envoyés par l'utilisateur dans la conversation.
     */
    List<File> findByConversationIdAndSenderId(Long conversationId, Long senderId);
}
