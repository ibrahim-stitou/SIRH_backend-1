package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupListDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupListDTO toDTO(Group group) {
        if (group == null) return null;

        GroupListDTO dto = new GroupListDTO();
        dto.setId(group.getId());

        dto.setHeadquartersId(group.getSiege() != null ? group.getSiege().getId() : null);

        dto.setName(group.getName());

        if (group.getManager() != null) {
            dto.setManagerId(group.getManager().getId());
            dto.setManagerName(group.getManager().getFirstName() + " " + group.getManager().getLastName());
        }

        dto.setCode(group.getCode());
        dto.setDescription(group.getDescription());

        dto.setCreatedAt(group.getCreatedAt());
        dto.setUpdatedAt(group.getUpdatedAt());

        return dto;
    }
}