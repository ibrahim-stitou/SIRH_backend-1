package com.tarmiz.SIRH_backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Purpose of the file",
        allowableValues = {"CIN", "PROFILE", "CV", "SALAIRE", "TRAVAIL", "STAGE", "SIGNED_CONTRACT","OTHER"}
)
public enum FilePurpose {
    CIN,
    PROFILE,
    CV,
    SALAIRE,
    TRAVAIL,
    STAGE   ,
    SIGNED_CONTRACT,
    GENERATED, SCANNED,
    OTHER
}