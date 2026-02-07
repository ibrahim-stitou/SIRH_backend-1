package com.tarmiz.SIRH_backend.model.DTO.ContractDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AvenantListDTO {

    private String id;
    private String reference;
    private String contractReference;
    private Integer numero;
    private String objet;
    private String typeModification;
    private LocalDate date;
    private String status;
    private String createdAt;
    private String documents;
}