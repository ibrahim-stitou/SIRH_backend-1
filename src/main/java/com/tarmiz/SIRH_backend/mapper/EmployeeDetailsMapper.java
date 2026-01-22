package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {
                DepartmentMapper.class,
                EmployeeSubResourceMapper.class,
        }
)
public interface EmployeeDetailsMapper {

    @Mappings({
            @Mapping(target = "status", expression = "java(employee.getStatus().getLabel())"),
            @Mapping(target = "gender", expression = "java(employee.getGender() != null ? employee.getGender().name() : null)"),
            @Mapping(target = "maritalStatus", expression = "java(employee.getMaritalStatus() != null ? employee.getMaritalStatus().name() : null)"),
            @Mapping(target = "peopleInCharge", source = "emergencyContacts"),
            @Mapping(target = "education", source = "educationList"),
            @Mapping(target = "skills", source = "skills"),
            @Mapping(target = "certifications", source = "certifications"),
            @Mapping(target = "experiences", source = "experiences"),
            @Mapping(target = "department", source = "department"),
            @Mapping(target = "address", source = "address")
    })
    EmployeeDetailsDTO toResponse(Employee employee);
}