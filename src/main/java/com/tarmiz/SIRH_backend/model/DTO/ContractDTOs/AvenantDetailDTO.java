package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvenantDetailDTO {

    /* ===================== Avenant infos ===================== */

    private String id;
    private String reference;

    private String contractId;
    private String contractReference;

    private String EmployeeName ;

    private Integer numero;
    private String date;

    private String objet;
    private String motif;
    private String description;

    private String typeModification;
    private String status;

    private String createdAt;
    private String createdBy;

    private List<String> actions;

    /* ===================== Changes ===================== */

    private ChangesDTO changes;

    /* ===================== Validations ===================== */

    private ValidationsDTO validations;

    /* ===================== Notes & documents ===================== */

    private String notes;
    private String documentUrl;
    private SignedDocumentDTO signedDocument;

    /* ========================================================= */
    /* ===================== INNER DTOs ======================== */
    /* ========================================================= */

    @Getter
    @Setter
    public static class ChangesDTO {
        private SalaryDetailDTO salary;
        private ScheduleDetailDTO schedule;
        private JobDetailDTO job;
    }

    /* --------------------- Salary --------------------- */

    @Getter
    @Setter
    public static class SalaryDetailDTO {
        private SalaryInfoDTO avant;
        private SalaryInfoDTO apres;
    }

    @Getter
    @Setter
    public static class SalaryInfoDTO {
        private Integer salaryBrut;
        private Integer salaryNet;
        private String currency;
        private String paymentMethod;
    }

    /* --------------------- Schedule --------------------- */

    @Getter
    @Setter
    public static class ScheduleDetailDTO {
        private ScheduleInfoDTO avant;
        private ScheduleInfoDTO apres;
    }

    @Getter
    @Setter
    public static class ScheduleInfoDTO {
        private String scheduleType;
        private Boolean shiftWork;
        private Integer annualLeaveDays;
        private String otherLeaves;
    }

    /* --------------------- Job --------------------- */

    @Getter
    @Setter
    public static class JobDetailDTO {
        private JobInfoDTO avant;
        private JobInfoDTO apres;
    }

    @Getter
    @Setter
    public static class JobInfoDTO {
        private String posteId;
        private String departementId;
        private String workMode;
        private String classification;
        private String level;
        private String responsibilities;
    }

    /* --------------------- Validations --------------------- */

    @Getter
    @Setter
    public static class ValidationsDTO {
        private Boolean manager;
        private Boolean rh;
    }

    /* --------------------- Signed document --------------------- */

    @Getter
    @Setter
    public static class SignedDocumentDTO {
        private String url;
        private String name;
        private String uploadedAt;
    }
}