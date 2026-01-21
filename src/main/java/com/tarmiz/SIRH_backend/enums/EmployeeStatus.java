package com.tarmiz.SIRH_backend.enums;

public enum EmployeeStatus {
    ACTIF("Actif"),
    SUSPENDU("Suspendu"),
    PARTI("Parti");

    private final String label;

    EmployeeStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}