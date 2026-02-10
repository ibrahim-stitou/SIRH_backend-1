package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractDetailsDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractJob;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSalary;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSchedule;
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

        // ===== Job (get active job) =====
        ContractJob activeJob = getActiveJob(contract);
        if (activeJob != null) {
            ContractDetailsDTO.JobDTO jobDto = new ContractDetailsDTO.JobDTO();
            jobDto.setMetier(activeJob.getPoste().getEmploi().getMetier().getLibelle());
            jobDto.setEmploi(activeJob.getPoste().getEmploi().getLibelle());
            jobDto.setPoste(activeJob.getPoste().getLibelle());
            jobDto.setPosteId(activeJob.getPoste().getId());
            jobDto.setLevel(activeJob.getLevel() != null
                    ? activeJob.getLevel().name()
                    : null);
            jobDto.setWorkMode(activeJob.getWorkMode() != null
                    ? activeJob.getWorkMode().name()
                    : null);
            jobDto.setClassification(activeJob.getClassification());
            jobDto.setResponsibilities(activeJob.getResponsibilities());
            dto.setJob(jobDto);
        }

        // ===== Schedule (get active schedule) =====
        ContractSchedule activeSchedule = getActiveSchedule(contract);
        if (activeSchedule != null) {
            ContractDetailsDTO.ScheduleDTO schedDto = new ContractDetailsDTO.ScheduleDTO();
            schedDto.setScheduleType(activeSchedule.getScheduleType() != null
                    ? activeSchedule.getScheduleType().name()
                    : null);
            schedDto.setShiftWork(activeSchedule.getShiftWork());
            schedDto.setHoursPerDay(activeSchedule.getHoursPerDay());
            schedDto.setDaysPerWeek(activeSchedule.getDaysPerWeek());
            schedDto.setHoursPerWeek(activeSchedule.getHoursPerWeek());
            schedDto.setStartTime(activeSchedule.getStartTime());
            schedDto.setEndTime(activeSchedule.getEndTime());
            schedDto.setBreakDuration(activeSchedule.getBreakDuration());
            schedDto.setAnnualLeaveDays(activeSchedule.getAnnualLeaveDays());
            schedDto.setOtherLeaves(activeSchedule.getOtherLeaves());
            dto.setSchedule(schedDto);
        }

        // ===== Salary (get active salary) =====
        ContractSalary activeSalary = getActiveSalary(contract);
        if (activeSalary != null) {
            ContractDetailsDTO.SalaryDTO salaryDto = new ContractDetailsDTO.SalaryDTO();
            salaryDto.setBaseSalary(activeSalary.getBaseSalary());
            salaryDto.setSalaryBrut(activeSalary.getSalaryBrut());
            salaryDto.setSalaryNet(activeSalary.getSalaryNet());
            salaryDto.setCurrency(activeSalary.getCurrency());
            salaryDto.setPaymentMethod(activeSalary.getPaymentMethod() != null
                    ? activeSalary.getPaymentMethod().name()
                    : null);
            salaryDto.setPeriodicity(activeSalary.getPeriodicity() != null
                    ? activeSalary.getPeriodicity().name()
                    : null);
            salaryDto.setPaymentDay(activeSalary.getPaymentDay());

            // Map primes if they exist
            if (activeSalary.getPrimes() != null && !activeSalary.getPrimes().isEmpty()) {
                salaryDto.setPrimes(activeSalary.getPrimes().stream()
                        .map(prime -> {
                            ContractDetailsDTO.PrimeDTO primeDto = new ContractDetailsDTO.PrimeDTO();
                            primeDto.setPrimeTypeId(prime.getPrimeTypeId() != null
                                    ? prime.getPrimeTypeId().name()
                                    : null);
                            primeDto.setLabel(prime.getLabel());
                            primeDto.setAmount(prime.getAmount());
                            primeDto.setIsTaxable(prime.getIsTaxable());
                            primeDto.setIsSubjectToCnss(prime.getIsSubjectToCnss());
                            return primeDto;
                        }).collect(Collectors.toList()));
            }

            dto.setSalary(salaryDto);
        }

        // ===== Clauses =====
        if (contract.getContractClauses() != null && !contract.getContractClauses().isEmpty()) {
            dto.setConditions(contract.getContractClauses().stream()
                    .map(cc -> {
                        ContractDetailsDTO.ClauseDTO clauseDto = new ContractDetailsDTO.ClauseDTO();
                        clauseDto.setId(cc.getClause().getId());
                        clauseDto.setTitle(cc.getClause().getName());
                        clauseDto.setDescription(cc.getClause().getDescription());
                        return clauseDto;
                    }).collect(Collectors.toList()));
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

    /**
     * Get the active job for a contract
     */
    private ContractJob getActiveJob(Contract contract) {
        if (contract.getJobs() == null || contract.getJobs().isEmpty()) {
            return null;
        }
        return contract.getJobs().stream()
                .filter(j -> j.getActive() != null && j.getActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the active salary for a contract
     */
    private ContractSalary getActiveSalary(Contract contract) {
        if (contract.getSalaries() == null || contract.getSalaries().isEmpty()) {
            return null;
        }
        return contract.getSalaries().stream()
                .filter(s -> s.getActive() != null && s.getActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the active schedule for a contract
     */
    private ContractSchedule getActiveSchedule(Contract contract) {
        if (contract.getSchedules() == null || contract.getSchedules().isEmpty()) {
            return null;
        }
        return contract.getSchedules().stream()
                .filter(sc -> sc.getActive() != null && sc.getActive())
                .findFirst()
                .orElse(null);
    }
}