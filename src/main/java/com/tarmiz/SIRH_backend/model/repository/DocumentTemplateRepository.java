package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentTemplateRepository
        extends JpaRepository<DocumentTemplate, Long> {

    List<DocumentTemplate>
    findByPurposeAndActiveTrue(FilePurpose purpose);
}