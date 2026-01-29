package com.tarmiz.SIRH_backend.service.document;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.Files.File;

import java.util.Map;

public interface PdfGeneratorService {
    File generateAndStore(
            EntityType entityType,
            Long entityId,
            FilePurpose purpose,
            Map<String, String> placeholders,
            String title,
            String description
    );
}