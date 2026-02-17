package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateGroupMembersDTO {
    private List<MemberDTO> members;

    @Getter
    @Setter
    public static class MemberDTO {
        private Long employeeId;
    }
}