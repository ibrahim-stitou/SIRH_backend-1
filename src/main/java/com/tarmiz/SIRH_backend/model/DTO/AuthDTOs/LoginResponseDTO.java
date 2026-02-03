package com.tarmiz.SIRH_backend.model.DTO.AuthDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
