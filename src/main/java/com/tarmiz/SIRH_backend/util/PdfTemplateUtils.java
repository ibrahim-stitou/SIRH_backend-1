package com.tarmiz.SIRH_backend.util;

import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class PdfTemplateUtils {

    public String loadHtml(String path) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessException("Cannot load template: " + path, e);
        }
    }

    public byte[] renderPdf(String html) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new BusinessException("PDF generation failed", e);
        }
    }

    public FilePurpose mapPurpose(AttestationType type) {
        return switch (type) {
            case SALARY -> FilePurpose.SALAIRE;
            case EMPLOYMENT -> FilePurpose.TRAVAIL;
            case EXPERIENCE -> FilePurpose.STAGE;
            default -> FilePurpose.OTHER;
        };
    }
}