package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;

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

        // Job: metier, emploi, poste
        if (contract.getJob() != null && contract.getJob().getPoste() != null) {
            var poste = contract.getJob().getPoste();

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

        // Schedule
        if (contract.getSchedule() != null) {
            ContractListDTO.ScheduleDTO scheduleDTO = new ContractListDTO.ScheduleDTO();
            scheduleDTO.setScheduleType(contract.getSchedule().getScheduleType() != null ?
                    contract.getSchedule().getScheduleType().name() : null);
            scheduleDTO.setShiftWork(contract.getSchedule().getShiftWork() != null ?
                    contract.getSchedule().getShiftWork() : false);
            dto.setSchedule(scheduleDTO);
        }

        // Dates
        dto.setDateDebut(contract.getStartDate());
        dto.setDateFin(contract.getEndDate());

        // Salary
        if (contract.getSalary() != null) {
            ContractListDTO.SalaryDTO salaryDTO = new ContractListDTO.SalaryDTO();
            salaryDTO.setSalaryBrut(contract.getSalary().getSalaryBrut() != null ?
                    contract.getSalary().getSalaryBrut().doubleValue() : null);
            salaryDTO.setSalaryNet(contract.getSalary().getSalaryNet() != null ?
                    contract.getSalary().getSalaryNet().doubleValue() : null);
            salaryDTO.setCurrency(contract.getSalary().getCurrency());
            dto.setSalary(salaryDTO);
        }

        dto.setStatut(contract.getStatus() != null ? contract.getStatus().name() : null);

        // Actions
        dto.setActions(1);

        return dto;
    }
}