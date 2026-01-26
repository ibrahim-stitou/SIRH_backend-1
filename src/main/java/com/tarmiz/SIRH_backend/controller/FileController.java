package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.File;
import com.tarmiz.SIRH_backend.service.FileService;
import com.tarmiz.SIRH_backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "Files",
        description = "Gestion des fichiers"
)
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /* ================= Upload ================= */

    @PostMapping
    public File upload(
            @RequestParam MultipartFile file,
            @RequestParam @ValidEnum(enumClass = EntityType.class) EntityType entityType,
            @RequestParam Long entityId,
            @RequestParam @ValidEnum(enumClass = FilePurpose.class) FilePurpose purpose,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        return fileService.upload(file, entityType, entityId, purpose, title, description);
    }

    /* ================= Query ================= */

    @GetMapping
    public List<File> list(
            @RequestParam @ValidEnum(enumClass = EntityType.class) EntityType entityType,
            @RequestParam Long entityId,
            @RequestParam(required = false) @ValidEnum(enumClass = FilePurpose.class) FilePurpose purpose
    ) {
        return purpose == null
                ? fileService.findByEntity(entityType, entityId)
                : fileService.findByEntityAndPurpose(entityType, entityId, purpose);
    }

    /* ================= Download ================= */

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        Resource resource = fileService.download(fileId);
        File meta = fileService.getMetadata(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + meta.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, meta.getMimeType())
                .body(resource);
    }

    /* ================= Delete ================= */

    @DeleteMapping("/{fileId}")
    public void delete(@PathVariable Long fileId) {
        fileService.delete(fileId);
    }
}
