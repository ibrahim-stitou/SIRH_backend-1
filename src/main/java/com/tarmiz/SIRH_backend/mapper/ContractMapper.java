package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractJob;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSalary;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSchedule;

public class ContractMapper {

    public static ContractListDTO toContractListDTO(Contract contract) {
        if (contract == null) return null;

        ContractListDTO dto = new ContractListDTO();

        dto.setId(contract.getId().toString());

        // Employe
        ContractListDTO.EmployeDTO employeDTO = new ContractListDTO.EmployeDTO();
        if (contract.getEmployee() != null) {
            employeDTO.setFullName(contract.getEmployee().getFirstName() + " " + contract.getEmployee().getLastName());
            employeDTO.setMatricule(contract.getEmployee().getMatricule());
        }
        dto.setEmploye(employeDTO);

        dto.setTypeDeContract(contract.getType() != null ? contract.getType().name() : null);

        // Job: metier, emploi, poste (get active job)
        ContractJob activeJob = getActiveJob(contract);
        if (activeJob != null && activeJob.getPoste() != null) {
            var poste = activeJob.getPoste();

            // Metier
            if (poste.getEmploi() != null && poste.getEmploi().getMetier() != null) {
                dto.setMetier(poste.getEmploi().getMetier().getLibelle());
            } else {
                dto.setMetier(null);
            }

            // Emploi
            if (poste.getEmploi() != null) {
                dto.setEmploi(poste.getEmploi().getLibelle());
            } else {
                dto.setEmploi(null);
            }

            // Poste
            dto.setPoste(poste.getLibelle());
        }

        // Schedule (get active schedule)
        ContractSchedule activeSchedule = getActiveSchedule(contract);
        if (activeSchedule != null) {
            ContractListDTO.ScheduleDTO scheduleDTO = new ContractListDTO.ScheduleDTO();
            scheduleDTO.setScheduleType(activeSchedule.getScheduleType() != null ?
                    activeSchedule.getScheduleType().name() : null);
            scheduleDTO.setShiftWork(activeSchedule.getShiftWork() != null ?
                    activeSchedule.getShiftWork() : false);
            dto.setSchedule(scheduleDTO);
        }

        // Dates
        dto.setDateDebut(contract.getStartDate());
        dto.setDateFin(contract.getEndDate());

        // Salary (get active salary)
        ContractSalary activeSalary = getActiveSalary(contract);
        if (activeSalary != null) {
            ContractListDTO.SalaryDTO salaryDTO = new ContractListDTO.SalaryDTO();
            salaryDTO.setSalaryBrut(activeSalary.getSalaryBrut() != null ?
                    activeSalary.getSalaryBrut().doubleValue() : null);
            salaryDTO.setSalaryNet(activeSalary.getSalaryNet() != null ?
                    activeSalary.getSalaryNet().doubleValue() : null);
            salaryDTO.setCurrency(activeSalary.getCurrency());
            dto.setSalary(salaryDTO);
        }

        dto.setStatut(contract.getStatus() != null ? contract.getStatus().name() : null);

        // Actions
        dto.setActions(1);

        return dto;
    }

    /**
     * Get the active job for a contract
     */
    private static ContractJob getActiveJob(Contract contract) {
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
    private static ContractSalary getActiveSalary(Contract contract) {
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
    private static ContractSchedule getActiveSchedule(Contract contract) {
        if (contract.getSchedules() == null || contract.getSchedules().isEmpty()) {
            return null;
        }
        return contract.getSchedules().stream()
                .filter(sc -> sc.getActive() != null && sc.getActive())
                .findFirst()
                .orElse(null);
    }
}