package com.tarmiz.SIRH_backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(
        description = "Department status",
        allowableValues = {"ACTIVE", "INACTIVE"}
)
@Getter
public enum DepartmentStatus {
    ACTIVE,
    INACTIVE
}
