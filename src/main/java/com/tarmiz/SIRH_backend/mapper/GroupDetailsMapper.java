package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.GroupDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.GroupMembersDTO;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.entity.Group;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupDetailsMapper {

    public GroupDetailsDTO toDetailsDTO(Group group) {
        if (group == null) return null;

        GroupDetailsDTO dto = new GroupDetailsDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setNameAr(group.getNameAr());
        dto.setCode(group.getCode());
        dto.setDescription(group.getDescription());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setUpdatedAt(group.getUpdatedAt());

        if (group.getManager() != null) {
            GroupDetailsDTO.ManagerInfo manager = new GroupDetailsDTO.ManagerInfo();
            manager.setId(group.getManager().getId());
            manager.setFullName(group.getManager().getFirstName() + " " + group.getManager().getLastName());
            dto.setManager(manager);
        }

        if (group.getSiege() != null) {
            GroupDetailsDTO.SiegeInfo siege = new GroupDetailsDTO.SiegeInfo();
            siege.setId(group.getSiege().getId());
            siege.setName(group.getSiege().getName());
            dto.setSiege(siege);
        }

        return dto;
    }

    public GroupMembersDTO toMembersDTO(Group group) {
        if (group == null) return null;

        GroupMembersDTO dto = new GroupMembersDTO();
        dto.setGroupId(group.getId());
        dto.setGroupName(group.getName());
        dto.setGroupCode(group.getCode());
        dto.setHeadquartersId(group.getSiege() != null ? group.getSiege().getId() : null);

        List<GroupMembersDTO.MemberInfo> members = new ArrayList<>();
        if (group.getEmployees() != null) {
            for (Employee e : group.getEmployees()) {
                GroupMembersDTO.MemberInfo m = new GroupMembersDTO.MemberInfo();
                m.setId(e.getId());
                m.setFullName(e.getFirstName() + " " + e.getLastName());
                m.setMatricule(e.getMatricule());
                members.add(m);
            }
        }
        dto.setMembers(members);
        return dto;
    }
}