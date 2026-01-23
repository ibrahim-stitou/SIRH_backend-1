package com.tarmiz.SIRH_backend.enums;

public enum Nationality {
    MAROCAIN("Marocain(e)"),
    ETRANGER("Etranger(e)");

    private final String label;

    Nationality(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}