package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SiegeWithGroupsDTO {
    private Long id;
    private String name;
    private String code;
    private String city;
    private String country;
    private String address;
    private String email;
    private String phone;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private List<GroupInfo> groups;
    private Integer groupsCount;

    @Getter
    @Setter
    public static class GroupInfo {
        private Long id;
        private String name;
        private String code;
        private String description;
        private LocalDate createdAt;
        private LocalDate updatedAt;

        private ManagerInfo manager;

        @Getter
        @Setter
        public static class ManagerInfo {
            private Long id;
            private String fullName;
        }
    }
}