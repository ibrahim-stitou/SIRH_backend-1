package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeSubResourcesDTO.*;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeSubResourceMapper {

    /* ================= Address ================= */
    AddressDTO toDto(Address address);

    /* ================= Education ================= */
    EducationDTO toDto(Education education);

    /* ================= Certification ================= */
    CertificationDTO toDto(Certification certification);

    /* ================= Skill ================= */
    @Mapping(target = "name", source = "skillName")
    @Mapping(
            target = "level",
            expression = "java(skill.getSkillLevel())"
    )
    SkillDTO toDto(Skill skill);

    /* ================= Experience ================= */
    ExperienceDTO toDto(Experience experience);

    /* ================= Person In Charge ================= */
    @Mapping(
            target = "relationship",
            expression = "java(person.getRelationship().getLabel())"
    )
    PersonInChargeDTO toDto(PersonInCharge person);
}