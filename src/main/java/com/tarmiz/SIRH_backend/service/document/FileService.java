package com.tarmiz.SIRH_backend.service.document;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import com.tarmiz.SIRH_backend.model.repository.FilesRepos.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorageService storageService;

    public FileService(FileRepository fileRepository,
                       FileStorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    /* ================= Upload ================= */

    public File upload(MultipartFile multipartFile,
                       EntityType entityType,
                       Long entityId,
                       FilePurpose purpose,
                       String title,
                       String description) {

        // Soft-delete existing active file for same (entityType, entityId, purpose)
        fileRepository.findByEntityTypeAndEntityIdAndPurposeAndDeletedAtIsNull(
                entityType, entityId, purpose
        ).forEach(f -> {
            f.setDeletedAt(LocalDateTime.now());
            fileRepository.save(f);
        });

        String storageKey = entityType + "/" + entityId + "/" + purpose + "/" + multipartFile.getOriginalFilename();
        String storagePath = storageService.store(multipartFile, storageKey);

        File file = new File();
        file.setEntityType(entityType);
        file.setEntityId(entityId);
        file.setPurpose(purpose);
        file.setTitle(title);
        file.setDescription(description);
        file.setFileName(multipartFile.getOriginalFilename());
        file.setMimeType(multipartFile.getContentType());
        file.setStoragePath(storagePath);
        file.setUploadedAt(LocalDateTime.now());
        file.setUploadedBy("system");

        return fileRepository.save(file);
    }

    /* ================= Queries ================= */

    public List<File> findByEntity(EntityType entityType, Long entityId) {
        return fileRepository.findByEntityTypeAndEntityIdAndDeletedAtIsNull(entityType, entityId);
    }

    public List<File> findByEntityAndPurpose(EntityType entityType, Long entityId, FilePurpose purpose) {
        return fileRepository.findByEntityTypeAndEntityIdAndPurposeAndDeletedAtIsNull(entityType, entityId, purpose);
    }

    public Resource download(Long fileId) {
        File file = fileRepository.findByIdAndDeletedAtIsNull(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));
        return storageService.loadAsResource(file.getStoragePath());
    }

    public File getMetadata(Long fileId) {
        return fileRepository.findByIdAndDeletedAtIsNull(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));
    }

    /* ================= Delete ================= */

    public void delete(Long fileId) {
        File file = fileRepository.findByIdAndDeletedAtIsNull(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));
        file.setDeletedAt(LocalDateTime.now());
        fileRepository.save(file);
    }
}