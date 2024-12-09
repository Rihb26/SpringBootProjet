package com.example.SpringProjet.Service;

import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import com.example.SpringProjet.Repository.File;
import com.example.SpringProjet.Repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ConversationRepository conversationRepository;
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private static final String UPLOAD_DIR = "uploads/";

    public FileService(FileRepository fileRepository, ConversationRepository conversationRepository) {
        this.fileRepository = fileRepository;
        this.conversationRepository = conversationRepository;
    }

    public File saveFile(Long conversationId, Long senderId, MultipartFile file) throws IOException {
        logger.info("Début de l'enregistrement du fichier : conversationId={}, senderId={}, fileName={}",
                conversationId, senderId, file.getOriginalFilename());

        // Vérifier si la conversation existe
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> {
                    logger.error("Conversation non trouvée pour conversationId={}", conversationId);
                    return new IllegalArgumentException("La conversation n'existe pas.");
                });

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            logger.error("Nom du fichier invalide pour le fichier envoyé par senderId={}", senderId);
            throw new IllegalArgumentException("Le fichier doit avoir un nom valide.");
        }

        // Créer le répertoire de sauvegarde si nécessaire
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Répertoire de sauvegarde créé : {}", uploadPath.toAbsolutePath());
        }

        // Générer un nom de fichier unique
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

        // Sauvegarde physique du fichier
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());
        logger.info("Fichier sauvegardé physiquement : {}", filePath.toAbsolutePath());

        // URL du fichier
        String fileUrl = "http://localhost:8080/" + UPLOAD_DIR.replace("\\", "/") + uniqueFileName;

        // Créer l'entité File
        File savedFile = new File();
        savedFile.setConversationId(conversationId);
        savedFile.setSenderId(senderId);
        savedFile.setName(originalFileName);
        savedFile.setFileUrl(fileUrl);
        savedFile.setPath(filePath.toString());
        savedFile.setUploadedAt(LocalDateTime.now());
        savedFile.setCreatedAt(LocalDateTime.now());

        File result = fileRepository.save(savedFile);
        logger.info("Fichier enregistré en base de données : fileId={}, fileName={}, fileUrl={}",
                result.getId(), result.getName(), result.getFileUrl());

        return result;
    }

    public void deleteFile(Long id) {
        logger.info("Suppression du fichier : fileId={}", id);

        if (!fileRepository.existsById(id)) {
            logger.error("Fichier introuvable pour suppression : fileId={}", id);
            throw new RuntimeException("File not found with id: " + id);
        }

        // Suppression dans la base de données
        fileRepository.deleteById(id);
        logger.info("Fichier supprimé de la base de données : fileId={}", id);
    }
}
