package com.tarmiz.SIRH_backend.model.entity.Attestation;

import com.tarmiz.SIRH_backend.enums.AttestationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "attestations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "request_id"),
                @UniqueConstraint(columnNames = "numero_attestation")
        })
public class Attestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private DemandeAttestation demandeAttestation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of attestation",
            example = "SALARY",
            allowableValues = {"SALARY", "EMPLOYMENT", "EXPERIENCE", "OTHER"})
    private AttestationType typeAttestation;

    @NotBlank
    @Column(name = "numero_attestation", nullable = false)
    private String numeroAttestation;

    private LocalDateTime dateGeneration;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}