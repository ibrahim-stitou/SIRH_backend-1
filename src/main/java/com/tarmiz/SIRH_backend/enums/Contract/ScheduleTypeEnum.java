package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type d'horaire / planning")
public enum ScheduleTypeEnum {
    FIXE,
    VARIABLE,
    FLEXIBLE,
    ROTATIF
}
