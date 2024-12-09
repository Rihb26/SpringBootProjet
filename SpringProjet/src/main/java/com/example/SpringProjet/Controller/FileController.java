package com.example.SpringProjet.Controller;

import com.example.SpringProjet.Repository.File;
import com.example.SpringProjet.Service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Endpoint pour uploader un fichier dans une conversation.
     *
     * @param conversationId L'ID de la conversation.
     * @param senderId        L'ID de l'utilisateur qui envoie le fichier.
     * @param file            Le fichier à envoyer.
     * @return Les informations du fichier sauvegardé.
     */
    @Operation(summary = "Upload de fichier")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<File> uploadFile(
            @RequestParam Long conversationId,
            @RequestParam Long senderId,
            @RequestPart("file") MultipartFile file) throws IOException {
        File uploadedFile = fileService.saveFile(conversationId, senderId, file);
        return ResponseEntity.ok(uploadedFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok("File deleted successfully");
    }




}
