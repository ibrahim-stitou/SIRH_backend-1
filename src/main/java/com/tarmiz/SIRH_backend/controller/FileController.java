package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.File;
import com.tarmiz.SIRH_backend.service.FileService;
import com.tarmiz.SIRH_backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Upload a file",
            description = "Upload a file and associate it with an entity and purpose"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = File.class)
            )
    )
    @PostMapping
    public File upload(
            @Parameter(description = "File to upload", required = true)
            @RequestParam MultipartFile file,

            @Parameter(
                    description = "Type of entity the file is associated with",
                    required = true,
                    schema = @Schema(implementation = EntityType.class)
            )
            @RequestParam @ValidEnum(enumClass = EntityType.class) EntityType entityType,

            @Parameter(description = "Identifier of the entity", required = true)
            @RequestParam Long entityId,

            @Parameter(
                    description = "Purpose of the file",
                    required = true,
                    schema = @Schema(implementation = FilePurpose.class)
            )
            @RequestParam @ValidEnum(enumClass = FilePurpose.class) FilePurpose purpose,

            @Parameter(description = "Optional title of the file")
            @RequestParam(required = false) String title,

            @Parameter(description = "Optional description of the file")
            @RequestParam(required = false) String description
    ) {
        return fileService.upload(file, entityType, entityId, purpose, title, description);
    }

    /* ================= Query ================= */

    @Operation(
            summary = "List files by entity and optional purpose",
            description = "Retrieve all files associated with a given entity, optionally filtered by purpose"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Files retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = File.class)
            )
    )
    @GetMapping
    public List<File> list(
            @Parameter(
                    description = "Entity type",
                    required = true,
                    schema = @Schema(implementation = EntityType.class)
            )
            @RequestParam @ValidEnum(enumClass = EntityType.class) EntityType entityType,

            @Parameter(description = "Identifier of the entity", required = true)
            @RequestParam Long entityId,

            @Parameter(
                    description = "Optional file purpose",
                    schema = @Schema(implementation = FilePurpose.class)
            )
            @RequestParam(required = false) @ValidEnum(enumClass = FilePurpose.class) FilePurpose purpose
    ) {
        return purpose == null
                ? fileService.findByEntity(entityType, entityId)
                : fileService.findByEntityAndPurpose(entityType, entityId, purpose);
    }

    /* ================= Download ================= */

    @Operation(
            summary = "Download a file",
            description = "Download a file by its ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File downloaded successfully"
    )
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(
            @Parameter(description = "File identifier", required = true)
            @PathVariable Long fileId
    ) {
        Resource resource = fileService.download(fileId);
        File meta = fileService.getMetadata(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + meta.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, meta.getMimeType())
                .body(resource);
    }

    /* ================= Delete ================= */

    @Operation(
            summary = "Delete a file",
            description = "Delete a file by its ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File deleted successfully"
    )
    @DeleteMapping("/{fileId}")
    public void delete(
            @Parameter(description = "File identifier", required = true)
            @PathVariable Long fileId
    ) {
        fileService.delete(fileId);
    }
}
