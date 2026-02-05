package com.example.Shop.service; // Dòng này phải khớp với thư mục

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service // Phải có Annotation này để Spring nhận diện
public class FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveFile(MultipartFile file) throws IOException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}