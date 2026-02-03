package com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagersDTO {
    private Long employeId;
    private Long departementId;

    private String employeeName;
    private String employeeFirstName;
    private String employeeLastName;

    private String departementName;
}
