package com.tarmiz.SIRH_backend.enums.Contract;

public enum PrimeTypeIdEnum {

    PRIME_TRANSPORT("Prime de Transport"),
    PRIME_PANIER("Prime Panier"),
    PRIME_ANCIENNETE("Prime d'Ancienneté"),
    PRIME_RESPONSABILITE("Prime de Responsabilité"),
    PRIME_PERFORMANCE("Prime de Performance"),
    PRIME_RISQUE("Prime de Risque"),
    PRIME_ASTREINTE("Prime d'Astreinte"),
    PRIME_TECHNICITE("Prime de Technicité");

    private final String label;

    PrimeTypeIdEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
