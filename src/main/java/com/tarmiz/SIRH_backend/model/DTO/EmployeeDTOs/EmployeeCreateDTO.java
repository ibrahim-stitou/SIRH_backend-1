package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import com.tarmiz.SIRH_backend.enums.*;
import com.tarmiz.SIRH_backend.validation.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class EmployeeCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String firstNameAr;
    private String lastNameAr;

    @NotBlank
    private String matricule;

    @NotBlank
    private String cin;

    @Past
    private LocalDate birthDate;

    private String birthPlace;

    @NotNull
    @ValidEnum(enumClass = Nationality.class)
    private String nationality;

    @NotNull
    @ValidEnum(enumClass = Gender.class)
    private String gender;

    @NotNull
    @ValidEnum(enumClass = MaritalStatus.class)
    private String maritalStatus;

    @Min(0)
    private Integer numberOfChildren;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long groupId;

    @NotNull
    @ValidEnum(enumClass = EmployeeStatus.class)
    private String status;

    // ================= Nested objects =================
    @Valid
    private EmployeeSubResourcesDTO.AddressDTO address;

    @Valid
    private List<EmployeeSubResourcesDTO.PersonInChargeDTO> emergencyContacts;

    @Valid
    private List<EmployeeSubResourcesDTO.SkillDTO> skills;

    @Valid
    private List<EmployeeSubResourcesDTO.EducationDTO> educationList;

    @Valid
    private List<EmployeeSubResourcesDTO.ExperienceDTO> experiences;

    @Valid
    private List<EmployeeSubResourcesDTO.CertificationDTO> certifications;
}