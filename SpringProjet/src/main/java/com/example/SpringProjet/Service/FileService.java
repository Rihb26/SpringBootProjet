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

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("La conversation n'existe pas."));


        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Le fichier doit avoir un nom valide.");
        }


        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());


        String fileUrl = "http://localhost:8080/" + UPLOAD_DIR.replace("\\", "/") + uniqueFileName;


        File savedFile = new File();
        savedFile.setConversationId(conversationId);
        savedFile.setSenderId(senderId);
        savedFile.setName(originalFileName);
        savedFile.setFileUrl(fileUrl);
        savedFile.setPath(filePath.toString());
        savedFile.setUploadedAt(LocalDateTime.now());
        savedFile.setCreatedAt(LocalDateTime.now());


        return fileRepository.save(savedFile);
    }

    public void deleteFile(Long id) {

        if (!fileRepository.existsById(id)) {
            throw new RuntimeException("File not found with id: " + id);
        }


        fileRepository.deleteById(id);
    }








}
