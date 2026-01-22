package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTOs internes à EmployeeResponseDTO
 * (Education, Skill, Experience, Certification, PersonInCharge, Address)
 */
@Getter
@Setter
public class EmployeeSubResourcesDTO {

    /* ================= Address ================= */
    public static class AddressDTO {
        public String street;
        public String city;
        public String postalCode;
        public String country;
    }

    /* ================= Education ================= */
    public static class EducationDTO {
        public String level;
        public String diploma;
        public Integer year;
        public String institution;
    }

    /* ================= Certification ================= */
    public static class CertificationDTO {
        public String name;
        public String issuer;
        public LocalDate issueDate;
        public LocalDate expirationDate;
    }

    /* ================= Skill ================= */
    public static class SkillDTO {
        public String name;
        public int level;
    }

    /* ================= Experience ================= */
    public static class ExperienceDTO {
        public String title;
        public String company;
        public LocalDate startDate;
        public LocalDate endDate;
        public String description;
    }

    /* ================= Person In Charge ================= */
    public static class PersonInChargeDTO {
        public String name;
        public String phone;
        public String cin;
        public LocalDate birthDate;
        public String relationship;
    }
}
