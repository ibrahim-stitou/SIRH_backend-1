package com.tarmiz.SIRH_backend.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Getter
    @Setter
    public static class EducationDTO {
        public String level;
        public String diploma;
        public Integer year;
        public String institution;
    }

    /* ================= Certification ================= */
    @Getter
    @Setter
    public static class CertificationDTO {
        public String name;
        public String issuer;
        public LocalDate issueDate;
        public LocalDate expirationDate;
    }

    /* ================= Skill ================= */
    @Getter
    @Setter
    public static class SkillDTO {
        public String name;
        public int level;
    }

    /* ================= Experience ================= */
    @Getter
    @Setter
    public static class ExperienceDTO {
        public String title;
        public String company;
        public LocalDate startDate;
        public LocalDate endDate;
        public String description;
    }

    /* ================= Person In Charge ================= */
    @Getter
    @Setter
    public static class PersonInChargeDTO {
        public String name;
        public String phone;
        public String cin;
        public LocalDate birthDate;
        @Schema(
                description = "Relationship to employee",
                example = "BROTHER",
                allowableValues = {"FATHER", "MOTHER", "BROTHER", "SISTER", "SPOUSE"}
        )
        public String relationship;
    }
}
