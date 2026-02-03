package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.enums.MaritalStatus;
import com.tarmiz.SIRH_backend.enums.Nationality;
import com.tarmiz.SIRH_backend.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String cin;
    private String numero_cnss;
    private LocalDate birthDate;
    private String birthPlace;
    @Schema(
            description = "Nationalité de l'employé",
            allowableValues = { "MAROCAIN", "ETRANGER" },
            example = "MAROCAIN"
    )
    @ValidEnum(enumClass = Nationality.class)
    private String nationality;
    @Schema(
            description = "Genre de l'employé",
            allowableValues = { "MALE", "FEMALE" },
            example = "FEMALE"
    )
    @ValidEnum(enumClass = Gender.class)
    private String gender;
    @Schema(
            description = "Situation matrimoniale de l'employé",
            allowableValues = { "CELIBATAIRE", "MARIE", "DIVORCE", "VEUF_VEUVE" },
            example = "MARIE"
    )
    @ValidEnum(enumClass = MaritalStatus.class)
    private String maritalStatus;
    private Integer numberOfChildren;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String email;
    private Long departmentId;
    private LocalDate hireDate;
    private String professionalCategory;
    @Schema(
            description = "Statut actuel de l'employé",
            allowableValues = { "ACTIF", "SUSPENDU", "PARTI" },
            example = "ACTIF"
    )
    @ValidEnum(enumClass = EmployeeStatus.class)
    private String status;
    private Boolean isActive;
}
