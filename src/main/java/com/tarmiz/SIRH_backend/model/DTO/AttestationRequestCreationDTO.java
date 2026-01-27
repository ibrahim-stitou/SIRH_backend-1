package com.tarmiz.SIRH_backend.model.DTO;

import com.tarmiz.SIRH_backend.enums.AttestationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttestationRequestCreationDTO {
    private Long employeeId;
    private AttestationType type;
    private LocalDate dateSouhaitee;
    private String note;
}
