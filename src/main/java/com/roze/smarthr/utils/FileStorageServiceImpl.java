package com.roze.smarthr.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${application.file.storage.path:./offer-letters}")
    private String storagePath;

    @Value("${smart-hr.base-url:http://localhost:8085}")
    private String baseUrl;

    public String storePdf(byte[] pdfBytes, String fileName) {
        try {
            Path dir = Paths.get(storagePath);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            Path filePath = dir.resolve(fileName);
            Files.write(filePath, pdfBytes);

            // Return absolute URL
            return baseUrl + "/api/v1/files/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store PDF", e);
        }
    }
}