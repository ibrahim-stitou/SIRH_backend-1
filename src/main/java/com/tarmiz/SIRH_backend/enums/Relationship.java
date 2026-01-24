package com.tarmiz.SIRH_backend.enums;

public enum Relationship {
    PERE("Père"),
    MERE("Mère"),
    FILS("Fils"),
    FILLE("Fille"),
    FRERE("Frère"),
    SOEUR("Soeur"),
    CONJOINT("Conjoint");

    private final String label;

    Relationship(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}