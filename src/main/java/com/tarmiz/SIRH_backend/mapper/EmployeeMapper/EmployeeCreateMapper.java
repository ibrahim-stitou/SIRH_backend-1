package com.tarmiz.SIRH_backend.mapper.EmployeeMapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeSubResourcesDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeCreateMapper {

    // ==================== Employee ====================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "address", source = "address")
    @Mapping(target = "emergencyContacts", source = "emergencyContacts")
    @Mapping(target = "skills", source = "skills")
    @Mapping(target = "educationList", source = "educationList")
    @Mapping(target = "experiences", source = "experiences")
    @Mapping(target = "certifications", source = "certifications")
    Employee toEntity(EmployeeCreateDTO dto);

    // ==================== Address ====================
    Address toEntity(EmployeeSubResourcesDTO.AddressDTO dto);

    // ==================== Person In Charge ====================
    PersonInCharge toEntity(EmployeeSubResourcesDTO.PersonInChargeDTO dto);

    // ==================== Skill ====================
    @Mapping(source = "name", target = "skillName")
    @Mapping(source = "level", target = "skillLevel")
    Skill toEntity(EmployeeSubResourcesDTO.SkillDTO dto);

    // ==================== Education ====================
    Education toEntity(EmployeeSubResourcesDTO.EducationDTO dto);

    // ==================== Experience ====================
    Experience toEntity(EmployeeSubResourcesDTO.ExperienceDTO dto);

    // ==================== Certification ====================
    Certification toEntity(EmployeeSubResourcesDTO.CertificationDTO dto);
}