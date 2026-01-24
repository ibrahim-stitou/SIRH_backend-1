package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.config.FileStorageProperties;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(FileStorageProperties props) {
        this.rootLocation = Paths.get(props.getBasePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file, String storageKey) {
        try {
            Path target = rootLocation.resolve(storageKey);
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public UrlResource loadAsResource(String storagePath) {
        try {
            Path path = Paths.get(storagePath);
            UrlResource resource = new UrlResource(path.toUri());
            if (!resource.exists()) throw new RuntimeException("File not found");
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found", e);
        }
    }

    public void delete(String storagePath) {
        try {
            Files.deleteIfExists(Paths.get(storagePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
