package com.tarmiz.SIRH_backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of an attestation request")
public enum AttestationDemandStatus {
    @Schema(description = "Request is pending approval") PENDING("En attente"),
    @Schema(description = "Request is approved") APPROVED("Approuvée"),
    @Schema(description = "Request is rejected") REJECTED("Rejetée"),
    @Schema(description = "Attestation has been generated") GENERATED("Générée"),
    @Schema(description = "Request is canceled") CANCELED("Annulée");

    private final String label;

    AttestationDemandStatus(String label) { this.label = label; }

    public String getLabel() { return label; }
}
