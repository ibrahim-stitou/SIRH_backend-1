package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Niveau de travail / échelle",
        example = "Echelle_1",
        allowableValues = {
                "Echelle_1",
                "Echelle_2",
                "Echelle_3",
                "Echelle_4",
                "Echelle_5",
                "Echelle_6",
                "Echelle_7",
                "Echelle_8",
                "Echelle_9",
                "Echelle_10",
                "Echelle_11",
                "Echelle_12",
                "Hors_echelle"
        }
)
public enum WorkLevelEnum {
    ECHELLE_1,
    ECHELLE_2,
    ECHELLE_3,
    ECHELLE_4,
    ECHELLE_5,
    ECHELLE_6,
    ECHELLE_7,
    ECHELLE_8,
    ECHELLE_9,
    ECHELLE_10,
    ECHELLE_11,
    ECHELLE_12,
    HORS_ECHELLE
}

