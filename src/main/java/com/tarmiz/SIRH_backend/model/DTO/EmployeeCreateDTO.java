package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeCreateDTO {
    private String firstName;
    private String lastName;
    private String firstNameAr;
    private String lastNameAr;
    private String matricule;
    private String cin;//
    private String numero_cnss;//
    private LocalDate birthDate;//
    private String birthPlace;//
    private String nationality;//
    private String gender;//
    private String maritalStatus;//
    private Integer numberOfChildren;//
    private String address;//
    private String city;//
    private String postalCode;//
    private String country;//
    private String phone;//
    private String email;//
    private Long departmentId;
    private LocalDate hireDate;
    private String professionalCategory;//
    private String status;
    private Boolean isActive;
}
