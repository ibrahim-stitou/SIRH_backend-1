package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttestationRequestDTO {

    private Long id;
    private Long requestId;
    private Long employeeId;
    private String typeAttestation;
    private LocalDate dateGeneration;
    private String numeroAttestation;
    private String documentPath;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}