package com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosteDetailDTO {
    private Long id;
    private String code;
    private String libelle;
    private String statut;
    private EmploiDTO emploi;
    private MetierDTO metier;
    private Long departmentId;
}