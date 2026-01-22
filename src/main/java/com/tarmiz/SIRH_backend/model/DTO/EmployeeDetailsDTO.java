package com.tarmiz.SIRH_backend.model.DTO;

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

    public String status;
    public String matricule;
    public String cin;

    public String gender;
    public String nationality;

    public LocalDate birthDate;
    public String birthPlace;

    public String maritalStatus;
    public Integer numberOfChildren;

    public String bankName;
    public String rib;

    public String aptitudeMedical;

    public DepartmentDTO department;
    public EmployeeSubResourcesDTO.AddressDTO address;

    public List<EmployeeSubResourcesDTO.EducationDTO> education;
    public List<EmployeeSubResourcesDTO.SkillDTO> skills;
    public List<EmployeeSubResourcesDTO.CertificationDTO> certifications;
    public List<EmployeeSubResourcesDTO.ExperienceDTO> experiences;
    public List<EmployeeSubResourcesDTO.PersonInChargeDTO> peopleInCharge;
}
