package com.tarmiz.SIRH_backend.model.DTO.AuthDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;
}
