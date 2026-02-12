package com.tarmiz.SIRH_backend.mapper.CompanyHierarchyMappers;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.ManagersDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public ManagersDTO toDto(Employee manager) {

        ManagersDTO dto = new ManagersDTO();
        dto.setEmployeId(manager.getId());
        dto.setEmployeeFirstName(manager.getFirstName());
        dto.setEmployeeLastName(manager.getLastName());
        dto.setEmployeeName(
                manager.getFirstName() + " " + manager.getLastName()
        );

        if (manager.getDepartment() != null) {
            dto.setDepartementId(manager.getDepartment().getId());
            dto.setDepartementName(manager.getDepartment().getName());
        }

        return dto;
    }
}
