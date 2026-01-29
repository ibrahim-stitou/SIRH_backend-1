package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Mode de travail",
        example = "Presentiel",
        allowableValues = {
                "Presentiel",
                "Hybride",
                "Teletravail",
                "Itinerant",
                "Horaire_variable"
        }
)
public enum WorkModeEnum { PRESENTIEL, HYBRIDE, TELETRAVAIL, ITINERANT, HORAIRE_VARIABLE }
