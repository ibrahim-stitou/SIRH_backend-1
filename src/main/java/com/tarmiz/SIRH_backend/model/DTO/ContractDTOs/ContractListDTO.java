package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ContractListDTO {

    private String id;
    private EmployeDTO employe;
    private String typeDeContract;
    private String metier; //metier.libelle
    private String emploi; //metier.libelle
    private String poste; //metier.libelle
    private ScheduleDTO schedule;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private SalaryDTO salary;
    private String statut;
    private int actions = 1;

    @Getter
    @Setter
    public static class EmployeDTO {
        private String fullName; // firstName + lastName
        private String matricule;
    }

    @Getter
    @Setter
    public static class ScheduleDTO {
        private String scheduleType;
        private boolean shiftWork;
    }

    @Getter
    @Setter
    public static class SalaryDTO {
        private Double salaryBrut;
        private Double salaryNet;
        private String currency;
    }
}