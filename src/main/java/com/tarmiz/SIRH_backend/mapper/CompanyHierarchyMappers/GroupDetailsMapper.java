package com.tarmiz.SIRH_backend.mapper.CompanyHierarchyMappers;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupMembersDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
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

        GroupMembersDTO dto = new GroupMembersDTO();

        dto.setGroupId(group.getId());
        dto.setGroupName(group.getName());
        dto.setGroupCode(group.getCode());

        if (group.getSiege() != null) {
            dto.setHeadquartersId(group.getSiege().getId());
        }

        List<GroupMembersDTO.MemberInfo> members = group.getEmployees()
                .stream()
                .map(employee -> toMemberInfo(employee, group.getManager()))
                .toList();

        dto.setMembers(members);

        return dto;
    }

    private GroupMembersDTO.MemberInfo toMemberInfo(Employee employee, Employee manager) {

        GroupMembersDTO.MemberInfo memberDTO = new GroupMembersDTO.MemberInfo();

        memberDTO.setId(employee.getId());
        memberDTO.setEmployeeId(employee.getId());

        boolean isManager = manager != null && employee.getId().equals(manager.getId());
        memberDTO.setManager(isManager);

        memberDTO.setEmployee(toEmployeeInfo(employee));

        return memberDTO;
    }

    private GroupMembersDTO.EmployeeInfo toEmployeeInfo(Employee employee) {

        GroupMembersDTO.EmployeeInfo dto = new GroupMembersDTO.EmployeeInfo();

        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setMatricule(employee.getMatricule());
        dto.setEmail(employee.getEmail());

        dto.setPosition(
                employee.getContracts().isEmpty()
                        ? null
                        : "Employee"
        );

        return dto;
    }
}