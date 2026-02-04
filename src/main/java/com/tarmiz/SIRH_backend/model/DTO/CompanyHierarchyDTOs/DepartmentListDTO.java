package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepartmentListDTO {
    private Long id;
    private String libelle;
    private String arLibelle;
    private String description;
    private boolean active;
    private LocalDateTime  createdAt;
    private LocalDateTime updatedAt;
}
