package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeIdNameDTO {
    private Long id;
    private String fullName;
}