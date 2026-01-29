package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Méthode de paiement",
        example = "Virement_Bancaire",
        allowableValues = { "Cheque", "Virement_Bancaire", "Espece" }
)
public enum PaymentMethodEnum {
    CHEQUE,
    VIREMENT_BANCAIRE,
    ESPECE
}
