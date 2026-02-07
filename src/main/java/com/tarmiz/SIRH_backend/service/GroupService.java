package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.exception.BusinessException.GroupNotEmptyException;
import com.tarmiz.SIRH_backend.mapper.GroupDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.GroupMapper;
import com.tarmiz.SIRH_backend.mapper.ManagerMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupListDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupMembersDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.ManagersDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.GroupRepository;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.SiegeRepository;
import com.tarmiz.SIRH_backend.util.Filtres.GroupSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SiegeRepository siegeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GroupDetailsMapper mapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private final ManagerMapper managerMapper;

    public ApiListResponse<GroupListDTO> getGroups(Integer start, Integer length, String dir,
                                                   String name, String code,
                                                   Long managerId, Long siegeId) {
        // Pageable
        Sort sort = Sort.by("createdAt");
        sort = "asc".equalsIgnoreCase(dir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(start / length, length, sort);

        // Specification
        Specification<Group> spec = Specification.where(GroupSpecifications.nameContains(name))
                .and(GroupSpecifications.codeContains(code))
                .and(GroupSpecifications.managerIdEquals(managerId))
                .and(GroupSpecifications.siegeIdEquals(siegeId));

        Page<Group> page = groupRepository.findAll(spec, pageable);

        List<GroupListDTO> dtos = page.getContent().stream()
                .map(groupMapper::toDTO)
                .toList();

        ApiListResponse<GroupListDTO> response = new ApiListResponse<>();
        response.setStatus("success");
        response.setMessage("Récupération réussie");
        response.setData(dtos);
        response.setRecordsTotal(groupRepository.count());
        response.setRecordsFiltered(page.getTotalElements());

        return response;
    }

    public ApiListResponse<GroupListDTO> getGroupsBySiege(Long siegeId, String nameFilter, String codeFilter) {
        Siege siege = siegeRepository.findById(siegeId)
                .orElseThrow(() -> new IllegalArgumentException("Siege not found with id: " + siegeId));

        List<Group> groups = groupRepository.findBySiege(siege);

        List<Group> filtered = groups.stream()
                .filter(g -> nameFilter == null || g.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                .filter(g -> codeFilter == null || g.getCode().toLowerCase().contains(codeFilter.toLowerCase()))
                .collect(Collectors.toList());

        List<GroupListDTO> dtos = filtered.stream()
                .map(groupMapper::toDTO)
                .collect(Collectors.toList());

        ApiListResponse<GroupListDTO> response = new ApiListResponse<>();
        response.setStatus("success");
        response.setMessage("Récupération réussie");
        response.setData(dtos);
        response.setRecordsTotal(groups.size());
        response.setRecordsFiltered(filtered.size());

        return response;
    }

    /* ================= Get Group Details ================= */
    public GroupDetailsDTO getGroupDetails(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));
        return mapper.toDetailsDTO(group);
    }

    /* ================= Get Group Members ================= */
    public GroupMembersDTO getGroupMembers(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));
        return mapper.toMembersDTO(group);
    }

    /* ================= Update Group Members ================= */
    @Transactional
    public Map<String, Object> updateGroupMembers(Long groupId, List<Long> add, List<Long> remove) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));

        List<Long> added = new ArrayList<>();
        List<Long> removed = new ArrayList<>();

        if (add != null) {
            for (Long empId : add) {
                Employee e = employeeRepository.findById(empId)
                        .orElseThrow(() -> new NoSuchElementException("Employee not found: " + empId));
                if (e.getGroup() != null) {
                    throw new BusinessException(
                            "Employee " + empId + " already belongs to a group");
                }
                group.getEmployees().add(e);
                e.setGroup(group);
                added.add(empId);
            }
        }

        if (remove != null) {
            for (Long empId : remove) {
                Employee e = employeeRepository.findById(empId)
                        .orElseThrow(() -> new NoSuchElementException("Employee not found: " + empId));
                if (!group.equals(e.getGroup())) {
                    throw new BusinessException(
                            "Employee " + empId + " does not belong to group " + groupId);
                }
                group.getEmployees().remove(e);
                e.setGroup(null);
                removed.add(empId);
            }
        }

        groupRepository.save(group);

        Map<String, Object> result = new HashMap<>();
        result.put("groupId", groupId);
        result.put("added", added);
        result.put("removed", removed);
        log.info("Updated group members for group {}", groupId);
        return result;
    }

    /* ================= Delete Group ================= */
    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);

        boolean hasEmployees = employeeRepository.existsByGroupId(groupId);
        if (hasEmployees) {
            throw new GroupNotEmptyException(groupId);
        }

        //group.setDeletedAt(LocalDateTime.now());
        groupRepository.save(group);
    }

    /* ================= Get Group by Employee ================= */
    public GroupListDTO getGroupByEmployee(Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        Group group = emp.getGroup();
        if (group == null) throw new NoSuchElementException("Group not found for employee");
        GroupListDTO dto = new GroupListDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setCode(group.getCode());
        dto.setHeadquartersId(group.getSiege() != null ? group.getSiege().getId() : null);
        return dto;
    }

    /* ================= Update Group Basic Info (PATCH) ================= */
    @Transactional
    public GroupDetailsDTO updateGroup(Long groupId, Map<String, Object> updates) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found"));

        if (updates.containsKey("name")) group.setName((String) updates.get("name"));
        if (updates.containsKey("code")) group.setCode((String) updates.get("code"));
        if (updates.containsKey("description")) group.setDescription((String) updates.get("description"));

        groupRepository.save(group);

        return mapper.toDetailsDTO(group);
    }

    public List<ManagersDTO> getAllManagers() {

        return groupRepository.findAllGroupsWithManagers()
                .stream()
                .map(Group::getManager)
                .distinct()
                .map(managerMapper::toDto)
                .toList();
    }
}