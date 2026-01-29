package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Conditions ou clauses spéciales du contrat",
        example = "COND_CONFIDENTIALITY",
        allowableValues = {
                "COND_CONFIDENTIALITY",
                "COND_NON_COMPETE",
                "COND_MOBILITY",
                "COND_INTELLECTUAL_PROPERTY",
                "COND_TRAINING",
                "COND_TELEWORK"
        }
)
public enum ContractConditionId {
    COND_CONFIDENTIALITY,
    COND_NON_COMPETE,
    COND_MOBILITY,
    COND_INTELLECTUAL_PROPERTY,
    COND_TRAINING,
    COND_TELEWORK
}