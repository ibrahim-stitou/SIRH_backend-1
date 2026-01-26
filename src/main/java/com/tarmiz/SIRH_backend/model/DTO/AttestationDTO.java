package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttestationDTO {

    private Long id;
    private Long employeeId;
    private String typeAttestation;
    private LocalDate dateRequest;
    private LocalDate dateSouhaitee;
    private String status;
    private String raison;
    private String notes;
    private LocalDateTime dateValidation;
    private LocalDateTime dateGeneration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}