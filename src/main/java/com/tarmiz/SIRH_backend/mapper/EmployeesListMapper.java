package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeesListMapper {
    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    @Mapping(target = "professionalCategory", constant = "employes_assimiles")
    @Mapping(target = "hireDate", constant = "01-01-2000")
    @Mapping(target = "contractType", constant = "CDI")
    @Mapping(target = "status", expression = "java(employee.getStatus().getLabel())")
    @Mapping(target = "actions", constant = "1")
    EmployeesListDTO toListItem(Employee employee);
}
