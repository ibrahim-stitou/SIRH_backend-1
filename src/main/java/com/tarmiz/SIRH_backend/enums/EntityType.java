package com.tarmiz.SIRH_backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Type of entity the file is associated with",
        allowableValues = {"EMPLOYEE", "CONTRACT", "ATTESTATION", "OTHER"}
)
public enum EntityType {
    EMPLOYEE, CONTRACT, ATTESTATION , OTHER
}
