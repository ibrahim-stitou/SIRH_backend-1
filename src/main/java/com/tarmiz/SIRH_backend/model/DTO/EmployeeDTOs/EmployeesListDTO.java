package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeesListDTO {
    private Long id;

    private String matricule;

    private String fullName;

    private String professionalCategory;

    private String email;

    private String hireDate;

    private String contractType;

    private String status;

    private Integer actions;
}
