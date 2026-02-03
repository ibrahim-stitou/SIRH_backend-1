package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiegeCreateDTO {
    private String name;
    private String code;
    private String street;
    private String city;
    private String country;
    private String phone;
    private String email;
}
