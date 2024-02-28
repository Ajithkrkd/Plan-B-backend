package com.ajith.userservice.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {
    public static String uploadFile (MultipartFile imageFile) throws IOException {
        String rootPath = System.getProperty("user.dir");
        String uploadDirectory = rootPath +"/src/main/resources/static/uploads";
        File dir = new File(uploadDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = imageFile.getOriginalFilename();
        String filePath = uploadDirectory + "/" + fileName;
        Path path = Paths.get(filePath);

        try {
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            // Handle the file copy exception
            throw new IOException("Failed to copy profile picture file", e);
        }
    }
}
