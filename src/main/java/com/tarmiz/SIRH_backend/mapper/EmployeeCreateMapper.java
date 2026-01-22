package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.entity.Address;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.enums.MaritalStatus;
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