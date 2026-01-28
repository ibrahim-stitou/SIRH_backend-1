package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SiegeListDTO {

    private Long id;
    private String name;
    private String code;
    private String address; // street
    private String city;
    private String country;
    private String phone;
    private String email;
    private int groupsCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private List<GroupInfo> groups;

    public static class GroupInfo {
        private Long id;
        private String name;
        private String code;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}