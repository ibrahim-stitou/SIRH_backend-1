package com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeDetailsDTO {
    public Long id;

    public String firstName;
    public String lastName;
    public String firstNameAr;
    public String lastNameAr;

    public String email;
    public String phone;

    @Schema(
            description = "Employment status of the employee",
            example = "ACTIF",
            allowableValues = {"ACTIF", "SUSPENDU", "PARTI"}
    )
    public String status;
    public String matricule;
    public String cin;

    @Schema(
            description = "Gender of the employee",
            example = "Male",
            allowableValues = {"Male", "Female"}
    )
    public String gender;
    @Schema(
            description = "Nationality of the employee",
            example = "MAROCAIN",
            allowableValues = {"MAROCAIN", "ETRANGER"}
    )
    public String nationality;

    public LocalDate birthDate;
    public String birthPlace;

    @Schema(
            description = "Marital status of the employee",
            example = "MARIE",
            allowableValues = {"CELIBATAIRE", "MARIE", "DIVORCE", "VEUF_VEUVE"}
    )
    public String maritalStatus;
    public Integer numberOfChildren;

    public String bankName;
    public String rib;

    @Schema(
            description = "Medical aptitude of the employee",
            example = "APTE",
            allowableValues = {
                    "APTE",
                    "APTE_AVEC_RESTRICTIONS",
                    "INAPTE_TEMPORAIRE",
                    "INAPTE_DEFINITIVE"
            }
    )
    public String aptitudeMedical;

    public DepartmentDTO department;
    public EmployeeSubResourcesDTO.AddressDTO address;

    public List<EmployeeSubResourcesDTO.EducationDTO> education;
    public List<EmployeeSubResourcesDTO.SkillDTO> skills;
    public List<EmployeeSubResourcesDTO.CertificationDTO> certifications;
    public List<EmployeeSubResourcesDTO.ExperienceDTO> experiences;
    public List<EmployeeSubResourcesDTO.PersonInChargeDTO> peopleInCharge;
}
