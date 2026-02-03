package com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosteDTO {
    private Long id;
    private String code;
    private String libelle;
    private String statut;
}
