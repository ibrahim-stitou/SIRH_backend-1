package com.tarmiz.SIRH_backend.model.DTO;

import com.tarmiz.SIRH_backend.enums.Contract.ContractConditionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractCreationDTO {

    private String reference;
    private String type;
    private String description;
    private Long employeId;

    private DatesDTO dates;
    private JobDTO job;
    private ScheduleDTO schedule;
    private SalaryDTO salary;
    private LegalDTO legal;
    private ConditionsDTO conditions;

    // ---------------- Nested DTOs ----------------

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatesDTO {
        private LocalDate signatureDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private TrialPeriodDTO trialPeriod;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrialPeriodDTO {
        private Boolean enabled;
        private Integer durationMonths;
        private Integer durationDays;
        private Boolean renewable;
        private Integer maxRenewals;
        private List<ContractConditionId> acceptanceCriteria;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
    public static class ScheduleDTO {
        private String scheduleType;
        private Boolean shiftWork;
        private Integer hoursPerDay;
        private Integer daysPerWeek;
        private Integer hoursPerWeek;
        private String startTime;
        private String endTime;
        private Integer breakDuration;
        private Integer annualLeaveDays;
        private String otherLeaves;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaryDTO {
        private BigDecimal salaryBrut;
        private BigDecimal salaryNet;
        private String currency;
        private String paymentMethod;
        private String periodicity;
        private List<PrimeDTO> primes;
        private AvantagesDTO avantages;
        private String indemnites;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
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
    public static class AvantagesDTO {
        private Boolean voiture;
        private Boolean logement;
        private Boolean telephone;
        private Boolean assuranceSante;
        private Boolean ticketsRestaurant;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LegalDTO {
        private Boolean cnssAffiliation;
        private Boolean amoAffiliation;
        private Boolean cmirAffiliation;
        private Boolean rcarAffiliation;
        private Boolean irApplicable;
        private Boolean assuranceGroupe;
        private Boolean clauseConfidentialite;
        private Boolean clauseNonConcurrence;
        private Boolean clauseMobilite;
        private String conventionCollective;
        private String conditionsSpeciales;
        private CotisationsDTO cotisations;
        private Integer dureePreavisJours;
        private BigDecimal indemniteDepart;
        private String notesLegales;
        private String cmirNumero;
        private Integer cmirTauxPct;
        private String rcarNumero;
        private Integer rcarTauxPct;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CotisationsDTO {
        private Double cnssEmployePct;
        private Double cnssEmployeurPct;
        private Double amoEmployePct;
        private Double amoEmployeurPct;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionsDTO {
        private List<ContractConditionId> selected;
    }
}