package com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupMembersDTO {
    private Long groupId;
    private String groupName;
    private String groupCode;
    private Long headquartersId;

    private List<MemberInfo> members;

    @Getter
    @Setter
    public static class MemberInfo {
        private Long id;
        private String matricule;
        private String fullName;
        private boolean isManager;
    }
}