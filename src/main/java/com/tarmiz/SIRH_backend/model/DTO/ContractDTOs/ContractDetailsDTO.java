package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContractDetailsDTO {

    private Long id;
    private String reference;
    private String type;
    private String status;
    private LocalDate signatureDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ClauseDTO> conditions;

    private String employeeId;
    private String employeeName;
    private String employeeMatricule;
    private String description;

    private TrialPeriodDTO trialPeriod;
    private JobDTO job;
    private ScheduleDTO schedule;
    private SalaryDTO salary;
    private LegalDTO legal;
    private List<SignedDocumentDTO> signedDocuments;
    private AuditDTO historique;

    /* ================= Nested DTOs ================= */

    @Getter
    @Setter
    public static class ClauseDTO {
        private Long id;
        private String title;
        private String description;
    }

    @Getter
    @Setter
    public static class TrialPeriodDTO {
        private Boolean enabled;
        private Integer durationMonths;
        private Integer durationDays;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean renewable;
        private Integer maxRenewals;
        private String conditions;
        private List<AcceptanceCriteriaDTO> acceptanceCriteria;
    }

    @Getter
    @Setter
    public static class AcceptanceCriteriaDTO {
        private Long id;
        private String label;
        private String description;
    }

    @Getter
    @Setter
    public static class JobDTO {
        private String metier;
        private String emploi;
        private String poste;
        private Long posteId;
        private String level;
        private String workMode;
        private String classification;
        private String responsibilities;
        private String description;
    }

    @Getter
    @Setter
    public static class ScheduleDTO {
        private String scheduleType;
        private Boolean shiftWork;
        private Integer hoursPerDay;
        private Integer daysPerWeek;
        private Integer hoursPerWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer breakDuration;
        private Integer annualLeaveDays;
        private String otherLeaves;
    }

    @Getter
    @Setter
    public static class SalaryDTO {
        private BigDecimal baseSalary;
        private BigDecimal salaryBrut;
        private BigDecimal salaryNet;
        private String currency;
        private String paymentMethod;
        private String periodicity;
        private List<PrimeDTO> primes;
        private AvantagesDTO avantages;
        private Integer paymentDay;
        private String indemnites;
    }

    @Getter
    @Setter
    public static class PrimeDTO {
        private String primeTypeId;
        private String label;
        private BigDecimal amount;
        private Boolean isTaxable;
        private Boolean isSubjectToCnss;
    }

    @Getter
    @Setter
    public static class AvantagesDTO {
        private Boolean voiture;
        private Boolean logement;
        private Boolean telephone;
        private Boolean assuranceSante;
        private Boolean ticketsRestaurant;
    }

    @Getter
    @Setter
    public static class LegalDTO {
        private Boolean cnssAffiliation;
        private Boolean amoAffiliation;
        private Boolean irApplicable;
        private Boolean clauseConfidentialite;
        private Boolean clauseNonConcurrence;
    }

    @Getter
    @Setter
    public static class SignedDocumentDTO {
        private String url;
        private String name;
        private LocalDateTime uploadedAt;
    }

    @Getter
    @Setter
    public static class AuditDTO {
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String updatedBy;
    }
}