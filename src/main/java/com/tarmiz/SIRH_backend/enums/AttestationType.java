package com.tarmiz.SIRH_backend.enums;

import lombok.Getter;

@Getter
public enum AttestationType {
    SALARY("Salaire"),
    EMPLOYMENT("Emploi"),
    EXPERIENCE("Expérience"),
    INTERNSHIP("Stage"),
    OTHER("Autre");

    private final String label;

    AttestationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}