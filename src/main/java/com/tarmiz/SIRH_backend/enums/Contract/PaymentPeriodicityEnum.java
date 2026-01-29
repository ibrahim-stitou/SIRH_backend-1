package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Type d'horaire pour un emploi",
        example = "Fixe",
        allowableValues = { "Fixe", "Variable", "Flexible", "Rotatif" }
)
public enum PaymentPeriodicityEnum {
    MENSUEL,
    BIMENSUEL,
    HEBDOMADAIRE
}
