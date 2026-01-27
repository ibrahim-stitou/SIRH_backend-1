package com.tarmiz.SIRH_backend.service.document;

import com.lowagie.text.DocumentException;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.DemandeAttestation;
import com.tarmiz.SIRH_backend.model.entity.File;

import java.io.IOException;
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