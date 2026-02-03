package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDTO toDto(Department department);
}