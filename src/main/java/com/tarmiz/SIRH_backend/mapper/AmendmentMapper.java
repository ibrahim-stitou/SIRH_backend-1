package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantDetailDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.*;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class, DateTimeFormatter.class})
public interface AmendmentMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "contract.reference", target = "contractReference")
    @Mapping(source = "numero", target = "numero")
    @Mapping(source = "objet", target = "objet")
    @Mapping(source = "typeModification", target = "typeModification")
    @Mapping(source = "amendmentDate", target = "date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdAt")
    AvenantListDTO toAvenantListDTO(Amendment amendment);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "contract.reference", target = "contractReference")
    @Mapping(target = "employeeName", expression = "java(amendment.getContract().getEmployee().getFirstName() + \" \" + amendment.getContract().getEmployee().getLastName())")
    @Mapping(source = "numero", target = "numero")
    @Mapping(source = "amendmentDate", target = "date")
    @Mapping(source = "objet", target = "objet")
    @Mapping(source = "motif", target = "motif")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "typeModification", target = "typeModification")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdAt")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(target = "changes", ignore = true)
    @Mapping(target = "actions", ignore = true)
    @Mapping(target = "validations", ignore = true)
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "documentUrl", ignore = true)
    @Mapping(target = "signedDocument", ignore = true)
    AvenantDetailDTO toAvenantDetailDTO(Amendment amendment);

    /* -------------------- AFTER MAPPING -------------------- */
    @AfterMapping
    default void populateChanges(Amendment amendment, @MappingTarget AvenantDetailDTO dto) {
        AvenantDetailDTO.ChangesDTO changes = new AvenantDetailDTO.ChangesDTO();
        LocalDate amendmentDate = amendment.getAmendmentDate();

        Contract contract = amendment.getContract();

        // ---------- Salary ----------
        AvenantDetailDTO.SalaryDetailDTO salary = new AvenantDetailDTO.SalaryDetailDTO();

        // "avant" = dernier salary avant la date de l'avenant
        ContractSalary avantSalary = contract.getSalaries().stream()
                .filter(s -> s.getEffectiveDate() != null && s.getEffectiveDate().isBefore(amendmentDate))
                .max(Comparator.comparing(ContractSalary::getEffectiveDate))
                .orElseGet(() -> contract.getSalaries().stream()
                        .filter(s -> s.getAmendment() == null)
                        .min(Comparator.comparing(ContractSalary::getEffectiveDate))
                        .orElse(null));

        if (avantSalary != null) {
            AvenantDetailDTO.SalaryInfoDTO avant = new AvenantDetailDTO.SalaryInfoDTO();
            avant.setSalaryBrut(avantSalary.getSalaryBrut().intValue());
            avant.setSalaryNet(avantSalary.getSalaryNet().intValue());
            avant.setCurrency(avantSalary.getCurrency());
            avant.setPaymentMethod(avantSalary.getPaymentMethod().name());
            salary.setAvant(avant);
        }

        // "apres" = salary de l'avenant
        ContractSalary apresSalary = contract.getSalaries().stream()
                .filter(s -> amendment.equals(s.getAmendment()))
                .findFirst()
                .orElse(null);

        if (apresSalary != null) {
            AvenantDetailDTO.SalaryInfoDTO apres = new AvenantDetailDTO.SalaryInfoDTO();
            apres.setSalaryBrut(apresSalary.getSalaryBrut().intValue());
            apres.setSalaryNet(apresSalary.getSalaryNet().intValue());
            apres.setCurrency(apresSalary.getCurrency());
            apres.setPaymentMethod(apresSalary.getPaymentMethod().name());
            salary.setApres(apres);
        }
        changes.setSalary(salary);

        // ---------- Schedule ----------
        AvenantDetailDTO.ScheduleDetailDTO schedule = new AvenantDetailDTO.ScheduleDetailDTO();

        ContractSchedule avantSchedule = contract.getSchedules().stream()
                .filter(s -> s.getEffectiveDate() != null && s.getEffectiveDate().isBefore(amendmentDate))
                .max(Comparator.comparing(ContractSchedule::getEffectiveDate))
                .orElseGet(() -> contract.getSchedules().stream()
                        .filter(s -> s.getAmendment() == null)
                        .min(Comparator.comparing(ContractSchedule::getEffectiveDate))
                        .orElse(null));

        if (avantSchedule != null) {
            AvenantDetailDTO.ScheduleInfoDTO avant = new AvenantDetailDTO.ScheduleInfoDTO();
            avant.setScheduleType(avantSchedule.getScheduleType().name());
            avant.setShiftWork(avantSchedule.getShiftWork());
            avant.setAnnualLeaveDays(avantSchedule.getAnnualLeaveDays());
            avant.setOtherLeaves(avantSchedule.getOtherLeaves());
            schedule.setAvant(avant);
        }

        ContractSchedule apresSchedule = contract.getSchedules().stream()
                .filter(s -> amendment.equals(s.getAmendment()))
                .findFirst()
                .orElse(null);

        if (apresSchedule != null) {
            AvenantDetailDTO.ScheduleInfoDTO apres = new AvenantDetailDTO.ScheduleInfoDTO();
            apres.setScheduleType(apresSchedule.getScheduleType().name());
            apres.setShiftWork(apresSchedule.getShiftWork());
            apres.setAnnualLeaveDays(apresSchedule.getAnnualLeaveDays());
            apres.setOtherLeaves(apresSchedule.getOtherLeaves());
            schedule.setApres(apres);
        }
        changes.setSchedule(schedule);

        // ---------- Job ----------
        AvenantDetailDTO.JobDetailDTO job = new AvenantDetailDTO.JobDetailDTO();

        ContractJob avantJob = contract.getJobs().stream()
                .filter(j -> j.getEffectiveDate() != null && j.getEffectiveDate().isBefore(amendmentDate))
                .max(Comparator.comparing(ContractJob::getEffectiveDate))
                .orElseGet(() -> contract.getJobs().stream()
                        .filter(j -> j.getAmendment() == null)
                        .min(Comparator.comparing(ContractJob::getEffectiveDate))
                        .orElse(null));

        if (avantJob != null) {
            AvenantDetailDTO.JobInfoDTO avant = new AvenantDetailDTO.JobInfoDTO();
            avant.setPosteId(avantJob.getPoste().getId().toString());
            avant.setWorkMode(avantJob.getWorkMode().name());
            avant.setClassification(avantJob.getClassification());
            avant.setLevel(avantJob.getLevel() == null ? null : avantJob.getLevel().name());
            avant.setResponsibilities(avantJob.getResponsibilities());
            job.setAvant(avant);
        }

        ContractJob apresJob = contract.getJobs().stream()
                .filter(j -> amendment.equals(j.getAmendment()))
                .findFirst()
                .orElse(null);

        if (apresJob != null) {
            AvenantDetailDTO.JobInfoDTO apres = new AvenantDetailDTO.JobInfoDTO();
            apres.setPosteId(apresJob.getPoste().getId().toString());
            apres.setWorkMode(apresJob.getWorkMode().name());
            apres.setClassification(apresJob.getClassification());
            apres.setLevel(apresJob.getLevel() == null ? null : apresJob.getLevel().name());
            apres.setResponsibilities(apresJob.getResponsibilities());
            job.setApres(apres);
        }
        changes.setJob(job);

        dto.setChanges(changes);
    }
}
