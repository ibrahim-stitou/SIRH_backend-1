package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Address;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeCreateMapper {

    @Mapping(target = "status", expression = "java(dto.getIsActive() != null && dto.getIsActive() ? com.tarmiz.SIRH_backend.enums.EmployeeStatus.ACTIF : com.tarmiz.SIRH_backend.enums.EmployeeStatus.SUSPENDU)")
    @Mapping(target = "gender", expression = "java(com.tarmiz.SIRH_backend.enums.Gender.valueOf(dto.getGender()))")
    @Mapping(target = "maritalStatus", expression = "java(com.tarmiz.SIRH_backend.enums.MaritalStatus.valueOf(dto.getMaritalStatus()))")
    @Mapping(target = "address", source = "dto", qualifiedByName = "toAddress")
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "educationList", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "emergencyContacts", ignore = true)

    @Mapping(target = "id", ignore = true)
    // Auditing fields
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)

    // Optional / later-managed fields
    @Mapping(target = "aptitudeMedical", ignore = true)
    @Mapping(target = "bankName", ignore = true)
    @Mapping(target = "rib", ignore = true)
    @Mapping(target = "group", ignore = true)
    Employee toEntity(EmployeeCreateDTO dto);

    @Named("toAddress")
    default Address toAddress(EmployeeCreateDTO dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.getAddress());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        return address;
    }
}