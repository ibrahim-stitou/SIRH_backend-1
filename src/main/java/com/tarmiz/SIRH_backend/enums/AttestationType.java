package com.tarmiz.SIRH_backend.enums;

public enum AttestationType {
    SALARY("Salaire"),
    EMPLOYMENT("Emploi"),
    EXPERIENCE("Expérience"),
    OTHER("Autre");

    private final String label;

    AttestationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}