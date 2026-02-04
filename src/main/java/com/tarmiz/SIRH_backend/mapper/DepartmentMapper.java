package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentListDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDTO toDto(Department department);

    @Mapping(target = "libelle", source = "name")
    @Mapping(target = "arLibelle", source = "nameAr")
    @Mapping(target = "active", expression = "java(department.getStatus() == com.tarmiz.SIRH_backend.enums.DepartmentStatus.ACTIVE)")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "updatedAt", source = "lastModifiedDate")
    DepartmentListDTO toListDto(Department department);
}