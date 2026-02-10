package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;

import com.tarmiz.SIRH_backend.enums.Contract.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreateAmendmentRequest {

    /* ===================== Avenant ===================== */

    @NotBlank
    private String reference;

    @NotNull
    private AmendmentType typeModification;

    @NotNull
    private LocalDate effectiveDate;

    @NotBlank
    private String objet;

    private String motif;
    private String description;
    private String notes;

    /* ===================== Changes ===================== */

    private ChangesDTO changes;

    /* =================================================== */

    @Getter
    @Setter
    public static class ChangesDTO {
        private SalaryChangeDTO salary;
        private ScheduleChangeDTO schedule;
        private JobChangeDTO job;
    }

    /* ---------------- Salary ---------------- */

    @Getter
    @Setter
    public static class SalaryChangeDTO {
        @NotNull
        private BigDecimal baseSalary;
        @NotNull
        private BigDecimal salaryBrut;
        @NotNull
        private BigDecimal salaryNet;

        private String currency;
        private PaymentMethodEnum paymentMethod;
        private PaymentPeriodicityEnum periodicity;
        private Integer paymentDay;
    }

    /* ---------------- Schedule ---------------- */

    @Getter
    @Setter
    public static class ScheduleChangeDTO {
        private ScheduleTypeEnum scheduleType;
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

    /* ---------------- Job ---------------- */

    @Getter
    @Setter
    public static class JobChangeDTO {
        @NotNull
        private Long posteId;

        private WorkModeEnum workMode;
        private String classification;
        private WorkLevelEnum level;
        private String responsibilities;
    }
}