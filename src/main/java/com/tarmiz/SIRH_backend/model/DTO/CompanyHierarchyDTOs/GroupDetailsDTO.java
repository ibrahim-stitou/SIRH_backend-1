package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GroupDetailsDTO {
    private Long id;
    private String name;
    private String nameAr;
    private String code;
    private String description;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private ManagerInfo manager;
    private SiegeInfo siege;

    @Getter
    @Setter
    public static class ManagerInfo {
        private Long id;
        private String fullName;
    }

    @Getter
    @Setter
    public static class SiegeInfo {
        private Long id;
        private String name;
    }
}