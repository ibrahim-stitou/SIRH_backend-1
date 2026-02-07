package com.tarmiz.SIRH_backend.model.repository.FilesRepos;

import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.Files.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentTemplateRepository
        extends JpaRepository<DocumentTemplate, Long> {

    List<DocumentTemplate>
    findByPurposeAndActiveTrue(FilePurpose purpose);
}