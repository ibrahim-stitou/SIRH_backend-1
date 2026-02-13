package com.tarmiz.SIRH_backend.mapper.CompanyHierarchyMappers;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeWithGroupsDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SiegeWithGroupsMapper {

    public SiegeWithGroupsDTO toDTO(Siege siege, String nameFilter, String codeFilter) {
        if (siege == null) return null;

        SiegeWithGroupsDTO dto = new SiegeWithGroupsDTO();
        dto.setId(siege.getId());
        dto.setName(siege.getName());
        dto.setCode(siege.getCode());
        dto.setPhone(siege.getPhone());
        dto.setEmail(siege.getEmail());
        dto.setCreatedAt(siege.getCreatedAt());
        dto.setUpdatedAt(siege.getUpdatedAt());

        if (siege.getAddress() != null) {
            dto.setCity(siege.getAddress().getCity());
            dto.setCountry(siege.getAddress().getCountry());
            dto.setAddress(siege.getAddress().getStreet());
        }

        // Filter and map groups
        List<Group> filteredGroups = siege.getGroups().stream()
                .filter(g -> nameFilter == null || g.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                .filter(g -> codeFilter == null || g.getCode().toLowerCase().contains(codeFilter.toLowerCase()))
                .toList();

        List<SiegeWithGroupsDTO.GroupInfo> groupInfos = filteredGroups.stream()
                .map(this::toGroupInfo)
                .toList();

        dto.setGroups(groupInfos);
        dto.setGroupsCount(groupInfos.size());

        return dto;
    }

    private SiegeWithGroupsDTO.GroupInfo toGroupInfo(Group group) {
        SiegeWithGroupsDTO.GroupInfo g = new SiegeWithGroupsDTO.GroupInfo();
        g.setId(group.getId());
        g.setName(group.getName());
        g.setCode(group.getCode());
        g.setDescription(group.getDescription());
        g.setCreatedAt(group.getCreatedAt());
        g.setUpdatedAt(group.getUpdatedAt());

        if (group.getManager() != null) {
            SiegeWithGroupsDTO.GroupInfo.ManagerInfo m = new SiegeWithGroupsDTO.GroupInfo.ManagerInfo();
            m.setId(group.getManager().getId());
            m.setFullName(group.getManager().getFirstName() + " " + group.getManager().getLastName());
            g.setManager(m);
        }

        return g;
    }
}