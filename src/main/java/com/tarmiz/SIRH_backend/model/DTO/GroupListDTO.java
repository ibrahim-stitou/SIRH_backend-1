package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GroupListDTO {

    private Long id;
    private Long headquartersId; // siege id
    private String name;
    private Long managerId;
    private String managerName;
    private String code;
    private String description;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
