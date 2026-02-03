package com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmploiDetailDTO {
    private Long id;
    private String code;
    private String libelle;
    private String statut;
    private MetierDTO metier;
    private List<PosteDTO> postes;
}