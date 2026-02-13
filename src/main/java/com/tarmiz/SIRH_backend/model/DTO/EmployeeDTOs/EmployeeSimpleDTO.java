package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSimpleDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String matricule;
}