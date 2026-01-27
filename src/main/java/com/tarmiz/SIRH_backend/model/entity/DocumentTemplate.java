package com.tarmiz.SIRH_backend.model.entity;

import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "document_templates")
public class DocumentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilePurpose purpose;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;
}