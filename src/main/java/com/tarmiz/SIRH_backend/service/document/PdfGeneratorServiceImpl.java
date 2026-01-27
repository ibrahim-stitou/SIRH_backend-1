package com.tarmiz.SIRH_backend.service.document;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.model.entity.DocumentTemplate;
import com.tarmiz.SIRH_backend.model.entity.File;
import com.tarmiz.SIRH_backend.model.repository.DocumentTemplateRepository;
import com.tarmiz.SIRH_backend.service.FileService;
import com.tarmiz.SIRH_backend.util.ByteArrayMultipartFile;
import com.tarmiz.SIRH_backend.util.PdfTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final DocumentTemplateRepository templateRepository;
    @Autowired
    private final PdfTemplateUtils helper;
    private final FileService fileService;

    @Override
    public File generateAndStore(
            EntityType entityType,
            Long entityId,
            FilePurpose purpose,
            Map<String, String> placeholders,
            String title,
            String description
    ) {
        log.info("Generating PDF for entityType={}, entityId={}, purpose={}", entityType, entityId, purpose);

        // 1️⃣ Load active template from DB
        List<DocumentTemplate> templates = templateRepository.findByPurposeAndActiveTrue(purpose);
        if (templates.isEmpty()) {
            log.error("No active template found for purpose {}", purpose);
            throw new BusinessException("No active template for purpose " + purpose);
        }
        if (templates.size() > 1) {
            log.warn("Multiple active templates found for purpose {}. Using the first one with id={}", purpose, templates.get(0).getId());
        }

        DocumentTemplate template = templates.get(0);
        log.debug("Using template id={}, filePath={}", template.getId(), template.getFilePath());

        try {
            // 2️⃣ Load HTML content
            String html = helper.loadHtml(template.getFilePath());
            log.debug("Loaded HTML template:\n{}", html);

            // 3️⃣ Replace placeholders
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue() : "";
                html = html.replace(placeholder, value);
                log.debug("Replaced placeholder {} with '{}'", placeholder, value);
            }

            // Optional: log final HTML before PDF generation
            log.trace("Final HTML before PDF rendering:\n{}", html);

            // 4️⃣ Generate PDF
            byte[] pdfBytes = helper.renderPdf(html);
            log.info("PDF generated, size={} bytes", pdfBytes.length);

            // 5️⃣ Convert to MultipartFile
            MultipartFile multipartFile = new ByteArrayMultipartFile(
                    pdfBytes,
                    purpose.name() + ".pdf",
                    purpose.name() + ".pdf",
                    "application/pdf"
            );

            // 6️⃣ Store via FileService
            File storedFile = fileService.upload(
                    multipartFile,
                    entityType,
                    entityId,
                    purpose,
                    title,
                    description
            );
            log.info("PDF stored successfully, fileId={}", storedFile.getId());

            return storedFile;

        } catch (Exception e) {
            log.error("PDF generation failed for entityType={}, entityId={}, purpose={}", entityType, entityId, purpose, e);
            throw new BusinessException("PDF generation failed", e);
        }
    }
}