package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractDetailsDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.model.entity.Contract.TrialPeriodCriteria;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ContractDetailsMapper {

    public ContractDetailsDTO toDTO(Contract contract, java.util.List<File> files) {
        ContractDetailsDTO dto = new ContractDetailsDTO();

        // ===== General Info =====
        dto.setId(contract.getId());
        dto.setReference(contract.getReference());
        dto.setType(contract.getType() != null ? contract.getType().name() : null);
        dto.setStatus(contract.getStatus() != null ? contract.getStatus().name() : null);
        dto.setSignatureDate(contract.getSignatureDate());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());

        dto.setEmployeeId(contract.getEmployee().getId().toString());
        dto.setEmployeeName(contract.getEmployee().getFirstName() + " " + contract.getEmployee().getLastName());
        dto.setEmployeeMatricule(contract.getEmployee().getMatricule());
        dto.setDescription(contract.getDescription());

        // ===== Trial Period =====
        if (contract.getTrialPeriod() != null) {
            ContractDetailsDTO.TrialPeriodDTO trialDto = new ContractDetailsDTO.TrialPeriodDTO();
            trialDto.setEnabled(contract.getTrialPeriod().getEnabled());
            trialDto.setDurationMonths(contract.getTrialPeriod().getDurationMonths());
            trialDto.setDurationDays(contract.getTrialPeriod().getDurationDays());
            trialDto.setStartDate(contract.getTrialPeriod().getStartDate());
            trialDto.setEndDate(contract.getTrialPeriod().getEndDate());
            trialDto.setRenewable(contract.getTrialPeriod().getRenewable());
            trialDto.setMaxRenewals(contract.getTrialPeriod().getMaxRenewals());
            trialDto.setConditions(contract.getTrialPeriod().getConditions());

            if (contract.getTrialPeriod().getTrialPeriodCriteriaList() != null) {
                trialDto.setAcceptanceCriteria(contract.getTrialPeriod().getTrialPeriodCriteriaList().stream()
                        .map(TrialPeriodCriteria::getTrialCriteria)
                        .map(tc -> {
                            ContractDetailsDTO.AcceptanceCriteriaDTO crit = new ContractDetailsDTO.AcceptanceCriteriaDTO();
                            crit.setId(tc.getId());
                            crit.setLabel(tc.getName());
                            crit.setDescription(tc.getDescription());
                            return crit;
                        }).collect(Collectors.toList()));
            }

            dto.setTrialPeriod(trialDto);
        }

        // ===== Job =====
        if (contract.getJob() != null) {
            ContractDetailsDTO.JobDTO jobDto = new ContractDetailsDTO.JobDTO();
            jobDto.setMetier(contract.getJob().getPoste().getEmploi().getMetier().getLibelle());
            jobDto.setEmploi(contract.getJob().getPoste().getEmploi().getLibelle());
            jobDto.setPoste(contract.getJob().getPoste().getLibelle());
            jobDto.setLevel(contract.getJob().getLevel() != null
                    ? contract.getJob().getLevel().name()
                    : null);
            jobDto.setWorkMode(contract.getJob().getWorkMode() != null
                    ? contract.getJob().getWorkMode().name()
                    : null);
            jobDto.setClassification(contract.getJob().getClassification());
            jobDto.setResponsibilities(contract.getJob().getResponsibilities());
            dto.setJob(jobDto);
        }

        // ===== Schedule =====
        if (contract.getSchedule() != null) {
            ContractDetailsDTO.ScheduleDTO schedDto = new ContractDetailsDTO.ScheduleDTO();
            schedDto.setScheduleType(contract.getSchedule().getScheduleType()!= null
                    ? contract.getSchedule().getScheduleType().name()
                    : null);
            schedDto.setShiftWork(contract.getSchedule().getShiftWork());
            schedDto.setHoursPerDay(contract.getSchedule().getHoursPerDay());
            schedDto.setDaysPerWeek(contract.getSchedule().getDaysPerWeek());
            schedDto.setHoursPerWeek(contract.getSchedule().getHoursPerWeek());
            schedDto.setStartTime(contract.getSchedule().getStartTime());
            schedDto.setEndTime(contract.getSchedule().getEndTime());
            schedDto.setBreakDuration(contract.getSchedule().getBreakDuration());
            schedDto.setAnnualLeaveDays(contract.getSchedule().getAnnualLeaveDays());
            schedDto.setOtherLeaves(contract.getSchedule().getOtherLeaves());
            dto.setSchedule(schedDto);
        }

        // ===== Salary =====
        if (contract.getSalary() != null) {
            ContractDetailsDTO.SalaryDTO salaryDto = new ContractDetailsDTO.SalaryDTO();
            salaryDto.setSalaryBrut(contract.getSalary().getSalaryBrut());
            salaryDto.setSalaryNet(contract.getSalary().getSalaryNet());
            salaryDto.setCurrency(contract.getSalary().getCurrency());
            salaryDto.setPaymentMethod(contract.getSalary().getPaymentMethod() != null ? contract.getSalary().getPaymentMethod().name() : null);
            salaryDto.setPeriodicity(contract.getSalary().getPeriodicity() != null ? contract.getSalary().getPeriodicity().name() : null);
            dto.setSalary(salaryDto);
        }

        // ===== Legal =====
        ContractDetailsDTO.LegalDTO legalDto = new ContractDetailsDTO.LegalDTO();
        legalDto.setCnssAffiliation(true);
        legalDto.setAmoAffiliation(true);
        legalDto.setIrApplicable(true);
        legalDto.setClauseConfidentialite(true);
        legalDto.setClauseNonConcurrence(true);
        dto.setLegal(legalDto);

        // ===== Signed Documents =====
        if (files != null && !files.isEmpty()) {
            dto.setSignedDocuments(files.stream()
                    .filter(f -> f.getEntityType() == EntityType.CONTRACT)
                    .map(f -> {
                        ContractDetailsDTO.SignedDocumentDTO doc = new ContractDetailsDTO.SignedDocumentDTO();
                        doc.setUrl(f.getStoragePath());
                        doc.setName(f.getFileName());
                        doc.setUploadedAt(f.getUploadedAt());
                        return doc;
                    }).collect(Collectors.toList()));
        }

        // ===== Audit =====
        ContractDetailsDTO.AuditDTO audit = new ContractDetailsDTO.AuditDTO();
        audit.setCreatedAt(contract.getCreatedDate());
        audit.setCreatedBy(contract.getCreatedBy());
        audit.setUpdatedAt(contract.getLastModifiedDate());
        audit.setUpdatedBy(contract.getLastModifiedBy());
        dto.setHistorique(audit);

        return dto;
    }
}