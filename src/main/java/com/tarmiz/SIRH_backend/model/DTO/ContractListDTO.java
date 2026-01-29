package com.tarmiz.SIRH_backend.model.DTO;

import com.tarmiz.SIRH_backend.enums.Contract.ContractConditionId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractListDTO {

    private Long id;
    private String reference;
    private String type;
    private String status;

    @Schema(description = "Employee ID")
    private String employeId;

    @Schema(description = "Employee full name")
    private String employeeName;

    @Schema(description = "Employee matricule")
    private String employeeMatricule;

    private String description;

    private DatesDTO dates;
    private JobDTO job;
    private ScheduleDTO schedule;
    private SalaryDTO salary;
    private LegalDTO legal;
    private ConditionsDTO conditions;
    private SignedDocumentDTO signedDocument;
    private HistoriqueDTO historique;

    private Integer actions = 1;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DatesDTO {
        private LocalDate signatureDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private TrialPeriodDTO trialPeriod;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class TrialPeriodDTO {
            private Boolean enabled;
            private Integer durationMonths;
            private Integer durationDays;
            private LocalDate endDate;
            private Boolean renewable;
            private Integer maxRenewals;
            private String conditions;
            private List<ContractConditionId> acceptanceCriteria;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobDTO {
        private String metier;
        private String emploie;
        private String poste;
        private String workMode;
        private String classification;
        private String workLocation;
        private String level;
        private String responsibilities;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SalaryDTO {
        private BigDecimal salaryBrut;
        private BigDecimal salaryNet;
        private String currency;
        private String paymentMethod;
        private String periodicity;
        private List<PrimeDTO> primes;
        private AvantagesDTO avantages;
        private String indemnites;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PrimeDTO {
            private String primeTypeId;
            private String label;
            private BigDecimal amount;
            private Boolean isTaxable;
            private Boolean isSubjectToCnss;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class AvantagesDTO {
            private Boolean voiture = false;
            private Boolean logement = false;
            private Boolean telephone = true;
            private Boolean assuranceSante = true;
            private Boolean ticketsRestaurant = true;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LegalDTO {
        private Boolean cnssAffiliation = true;
        private Boolean amoAffiliation = true;
        private Boolean irApplicable = true;
        private Boolean clauseConfidentialite = true;
        private Boolean clauseNonConcurrence = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConditionsDTO {
        private List<ContractConditionId> selected;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignedDocumentDTO {
        private String url;
        private String name;
        private LocalDateTime uploadedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HistoriqueDTO {
        private LocalDateTime createdAt;
        private String createdBy;
        private String createdByName;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private String updatedByName;
    }
}