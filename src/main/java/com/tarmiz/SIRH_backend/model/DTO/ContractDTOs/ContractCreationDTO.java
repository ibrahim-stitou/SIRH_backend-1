package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;

import com.tarmiz.SIRH_backend.enums.Contract.ContractTypeEnum;
import com.tarmiz.SIRH_backend.enums.Contract.PrimeTypeIdEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ContractCreationDTO {

    private String reference;
    private Long employeeId;
    private ContractTypeEnum type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signatureDate;
    private String description;

    private TrialPeriodDTO trialPeriod;
    private ContractJobDTO job;
    private ContractConditionsDTO conditions;
    private ContractScheduleDTO schedule;
    private ContractSalaryDTO salary;

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
    }

    @Getter
    @Setter
    public static class ContractJobDTO {
        private Long metierId;
        private Long emploiId;
        private Long posteId;

        private String workMode;
        private String classification;
        private Long siegeId;
        private String responsibilities;
    }

    @Getter
    @Setter
    public static class ContractConditionsDTO {
        private List<Long> selectedConditionIds;
    }

    @Getter
    @Setter
    public static class ContractScheduleDTO {
        private String scheduleType;
        private Boolean workShift;
    }

    @Getter
    @Setter
    public static class ContractSalaryDTO {
        private BigDecimal salaryBase;
        private BigDecimal salaryBrut;
        private BigDecimal salaryNet;
        private String currency;
        private String paymentMethod;
        private String periodicity;
        private List<PrimeDTO> primes;

        @Getter
        @Setter
        public static class PrimeDTO {
            private PrimeTypeIdEnum primeTypeId;
            private BigDecimal amount;
            private Boolean isTaxable;
            private Boolean isSubjectToCnss;
        }
    }
}