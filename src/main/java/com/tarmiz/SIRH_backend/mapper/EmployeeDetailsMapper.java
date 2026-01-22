package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.entity.Employee;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {
                DepartmentMapper.class,
                AddressMapper.class,
                EducationMapper.class,
                SkillMapper.class,
                CertificationMapper.class,
                ExperienceMapper.class,
                PersonInChargeMapper.class
        }
)
public interface EmployeeMapper {

    @Mappings({
            @Mapping(target = "status", expression = "java(employee.getStatus().getLabel())"),
            @Mapping(target = "gender", expression = "java(employee.getGender().getLabel())"),
            @Mapping(target = "maritalStatus", expression = "java(employee.getMaritalStatus().getLabel())"),
            @Mapping(target = "peopleInCharge", source = "emergencyContacts"),
            @Mapping(target = "education", source = "educationList")
    })
    EmployeeResponseDTO toResponse(Employee employee);
}