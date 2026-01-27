package com.tarmiz.SIRH_backend.enums;

public enum AttestationDemandStatus {
    PENDING("En attente"),
    APPROVED("Approuvée"),
    REJECTED("Rejetée"),
    GENERATED("Générée"),
    CANCELED("Annulée");

    private final String Label;

    AttestationDemandStatus(String Label) {
        this.Label = Label;
    }

    public String getLabel() {
        return Label;
    }
}