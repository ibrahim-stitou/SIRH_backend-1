package com.tarmiz.SIRH_backend.model.entity;

import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "demande_attestation")
public class DemandeAttestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @Schema(description = "Employee who requested the attestation")
    private Employee employee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of attestation requested",
            example = "SALARY",
            allowableValues = {"SALARY", "EMPLOYMENT", "EXPERIENCE", "OTHER"})
    private AttestationType typeAttestation;

    @NotNull
    private LocalDate dateRequest;

    @NotNull
    private LocalDate dateSouhaitee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of the request",
            example = "PENDING",
            allowableValues = {"PENDING", "APPROVED", "REJECTED"})
    private AttestationDemandStatus status;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime dateValidation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "demandeAttestation", cascade = CascadeType.ALL)
    private Attestation attestation;

}