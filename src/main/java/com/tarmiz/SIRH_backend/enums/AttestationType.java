package com.tarmiz.SIRH_backend.enums;

import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of attestation")
@Getter
public enum AttestationType {
    @Schema(description = "Salary attestation") SALARY("Salaire"),
    @Schema(description = "Employment attestation") EMPLOYMENT("Emploi"),
    @Schema(description = "Experience attestation") EXPERIENCE("Expérience"),
    @Schema(description = "Internship attestation") INTERNSHIP("Stage"),
    @Schema(description = "Other type") OTHER("Autre");

    private final String label;

    AttestationType(String label) { this.label = label; }

    public String getLabel() { return label; }
}
