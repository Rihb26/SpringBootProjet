package com.example.SpringProjet.Service;

import com.example.SpringProjet.Repository.Conversation;
import com.example.SpringProjet.Repository.ConversationRepository;
import com.example.SpringProjet.Repository.File;
import com.example.SpringProjet.Repository.FileRepository;
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

    private static final String UPLOAD_DIR = "uploads/";

    public FileService(FileRepository fileRepository, ConversationRepository conversationRepository) {
        this.fileRepository = fileRepository;
        this.conversationRepository = conversationRepository;
    }

    public File saveFile(Long conversationId, Long senderId, MultipartFile file) throws IOException {
        // Vérifier si la conversation existe
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("La conversation n'existe pas."));

        // Vérifier si le fichier a un nom valide
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Le fichier doit avoir un nom valide.");
        }

        // Créer le répertoire de sauvegarde s'il n'existe pas
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

        // Sauvegarder le fichier sur le disque
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());

        // Construire l'URL d'accès au fichier
        String fileUrl = "http://localhost:8080/" + UPLOAD_DIR.replace("\\", "/") + uniqueFileName;

        // Créer une entité File pour la base de données
        File savedFile = new File();
        savedFile.setConversationId(conversationId);
        savedFile.setSenderId(senderId);
        savedFile.setName(originalFileName); // Renseigner explicitement le champ "name"
        savedFile.setFileUrl(fileUrl);
        savedFile.setPath(filePath.toString());
        savedFile.setUploadedAt(LocalDateTime.now());
        savedFile.setCreatedAt(LocalDateTime.now());

        // Enregistrer les métadonnées dans la base de données
        return fileRepository.save(savedFile);
    }

    public void deleteFile(Long id) {
        // Vérifie si le fichier existe, sinon lève une exception
        if (!fileRepository.existsById(id)) {
            throw new RuntimeException("File not found with id: " + id);
        }

        // Supprime le fichier de la base de données
        fileRepository.deleteById(id);
    }








}
