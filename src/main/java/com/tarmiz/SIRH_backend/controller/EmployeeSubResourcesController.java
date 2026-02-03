package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.enums.Relationship;
import com.tarmiz.SIRH_backend.mapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeSubResourcesDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.*;
import com.tarmiz.SIRH_backend.service.EmployeeSubResourcesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Employees Infos",
        description = "Employee sub infos management operations"
)
@RestController
@RequestMapping("/hrEmployees/{employeeId}")
public class EmployeeSubResourcesController {

    private final EmployeeSubResourcesService subResourcesService;
    private final EmployeeDetailsMapper employeeDetailsMapper;

    public EmployeeSubResourcesController(EmployeeSubResourcesService subResourcesService,
                                          EmployeeDetailsMapper employeeDetailsMapper) {
        this.subResourcesService = subResourcesService;
        this.employeeDetailsMapper = employeeDetailsMapper;
    }

    // ================= Person In Charge =================
    @PostMapping("/emergency-contacts")
    @Operation( summary = "Add a new emergency contact", description = "Creates and associates a new emergency contact with the given employee." ) @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Emergency contact added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDetailsDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found"), @ApiResponse(responseCode = "400", description = "Invalid input data") })
    public ResponseEntity<EmployeeDetailsDTO> addPersonInCharge(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSubResourcesDTO.PersonInChargeDTO dto) {

        PersonInCharge entity = new PersonInCharge();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setCin(dto.getCin());
        entity.setBirthDate(dto.getBirthDate());
        if(dto.getRelationship() != null) entity.setRelationship(Relationship.valueOf(dto.getRelationship()));

        Employee updated = subResourcesService.addSubResource(
                employeeId,
                entity,
                Employee::getEmergencyContacts,
                (e, emp) -> e.setEmployee(emp)
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @PutMapping("/emergency-contacts/{contactId}")
    public ResponseEntity<EmployeeDetailsDTO> updatePersonInCharge(
            @PathVariable Long employeeId,
            @PathVariable Long contactId,
            @RequestBody EmployeeSubResourcesDTO.PersonInChargeDTO dto) {

        Employee updated = subResourcesService.updateSubResource(
                employeeId,
                contactId,
                dto,
                Employee::getEmergencyContacts,
                (entity, upd) -> {
                    if(upd.getName() != null) entity.setName(upd.getName());
                    if(upd.getPhone() != null) entity.setPhone(upd.getPhone());
                    if(upd.getCin() != null) entity.setCin(upd.getCin());
                    if(upd.getBirthDate() != null) entity.setBirthDate(upd.getBirthDate());
                    if(upd.getRelationship() != null) entity.setRelationship(Relationship.valueOf(upd.getRelationship()));
                },
                PersonInCharge::getId,
                "PersonInCharge"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @DeleteMapping("/emergency-contacts/{contactId}")
    public ResponseEntity<EmployeeDetailsDTO> deletePersonInCharge(
            @PathVariable Long employeeId,
            @PathVariable Long contactId) {

        Employee updated = subResourcesService.deleteSubResource(
                employeeId,
                contactId,
                Employee::getEmergencyContacts,
                "PersonInCharge"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    // ================= Skill =================
    @PostMapping("/skills")
    public ResponseEntity<EmployeeDetailsDTO> addSkill(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSubResourcesDTO.SkillDTO dto) {

        Skill entity = new Skill();
        entity.setSkillName(dto.getName());
        entity.setSkillLevel(dto.getLevel());

        Employee updated = subResourcesService.addSubResource(
                employeeId,
                entity,
                Employee::getSkills,
                (Skill s,Employee emp) -> s.setEmployee(emp)
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @PutMapping("/skills/{skillId}")
    public ResponseEntity<EmployeeDetailsDTO> updateSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId,
            @RequestBody EmployeeSubResourcesDTO.SkillDTO dto) {

        Employee updated = subResourcesService.updateSubResource(
                employeeId,
                skillId,
                dto,
                Employee::getSkills,
                (entity,upd) -> {
                    if(upd.getName() != null) entity.setSkillName(upd.getName());
                    if(upd.getLevel() != 0) entity.setSkillLevel(upd.getLevel());
                },
                Skill::getId,
                "Skill"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<EmployeeDetailsDTO> deleteSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId) {

        Employee updated = subResourcesService.deleteSubResource(
                employeeId,
                skillId,
                Employee::getSkills,
                "Skill"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    // ================= Education =================
    @PostMapping("/education")
    public ResponseEntity<EmployeeDetailsDTO> addEducation(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSubResourcesDTO.EducationDTO dto) {

        Education entity = new Education();
        entity.setLevel(dto.getLevel());
        entity.setDiploma(dto.getDiploma());
        entity.setYear(dto.getYear());
        entity.setInstitution(dto.getInstitution());

        Employee updated = subResourcesService.addSubResource(
                employeeId,
                entity,
                Employee::getEducationList,
                (e, emp) -> e.setEmployee(emp)
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @PutMapping("/education/{eduId}")
    public ResponseEntity<EmployeeDetailsDTO> updateEducation(
            @PathVariable Long employeeId,
            @PathVariable Long eduId,
            @RequestBody EmployeeSubResourcesDTO.EducationDTO dto) {

        Employee updated = subResourcesService.updateSubResource(
                employeeId,
                eduId,
                dto,
                Employee::getEducationList,
                (entity, upd) -> {
                    if(upd.getLevel() != null) entity.setLevel(upd.getLevel());
                    if(upd.getDiploma() != null) entity.setDiploma(upd.getDiploma());
                    if(upd.getYear() != null) entity.setYear(upd.getYear());
                    if(upd.getInstitution() != null) entity.setInstitution(upd.getInstitution());
                },
                Education::getId,
                "Education"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @DeleteMapping("/education/{eduId}")
    public ResponseEntity<EmployeeDetailsDTO> deleteEducation(
            @PathVariable Long employeeId,
            @PathVariable Long eduId) {

        Employee updated = subResourcesService.deleteSubResource(
                employeeId,
                eduId,
                Employee::getEducationList,
                "Education"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    // ================= Experience =================
    @PostMapping("/experiences")
    public ResponseEntity<EmployeeDetailsDTO> addExperience(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSubResourcesDTO.ExperienceDTO dto) {

        Experience entity = new Experience();
        entity.setTitle(dto.getTitle());
        entity.setCompany(dto.getCompany());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());

        Employee updated = subResourcesService.addSubResource(
                employeeId,
                entity,
                Employee::getExperiences,
                (e, emp) -> e.setEmployee(emp)
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @PutMapping("/experiences/{expId}")
    public ResponseEntity<EmployeeDetailsDTO> updateExperience(
            @PathVariable Long employeeId,
            @PathVariable Long expId,
            @RequestBody EmployeeSubResourcesDTO.ExperienceDTO dto) {

        Employee updated = subResourcesService.updateSubResource(
                employeeId,
                expId,
                dto,
                Employee::getExperiences,
                (entity, upd) -> {
                    if(upd.getTitle() != null) entity.setTitle(upd.getTitle());
                    if(upd.getCompany() != null) entity.setCompany(upd.getCompany());
                    if(upd.getStartDate() != null) entity.setStartDate(upd.getStartDate());
                    if(upd.getEndDate() != null) entity.setEndDate(upd.getEndDate());
                    if(upd.getDescription() != null) entity.setDescription(upd.getDescription());
                },
                Experience::getId,
                "Experience"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @DeleteMapping("/experiences/{expId}")
    public ResponseEntity<EmployeeDetailsDTO> deleteExperience(
            @PathVariable Long employeeId,
            @PathVariable Long expId) {

        Employee updated = subResourcesService.deleteSubResource(
                employeeId,
                expId,
                Employee::getExperiences,
                "Experience"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    // ================= Certification =================
    @PostMapping("/certifications")
    public ResponseEntity<EmployeeDetailsDTO> addCertification(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSubResourcesDTO.CertificationDTO dto) {

        Certification entity = new Certification();
        entity.setName(dto.getName());
        entity.setIssuer(dto.getIssuer());
        entity.setIssueDate(dto.getIssueDate());
        entity.setExpirationDate(dto.getExpirationDate());

        Employee updated = subResourcesService.addSubResource(
                employeeId,
                entity,
                Employee::getCertifications,
                (c, emp) -> c.setEmployee(emp)
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @PutMapping("/certifications/{certId}")
    public ResponseEntity<EmployeeDetailsDTO> updateCertification(
            @PathVariable Long employeeId,
            @PathVariable Long certId,
            @RequestBody EmployeeSubResourcesDTO.CertificationDTO dto) {

        Employee updated = subResourcesService.updateSubResource(
                employeeId,
                certId,
                dto,
                Employee::getCertifications,
                (entity, upd) -> {
                    if(upd.getName() != null) entity.setName(upd.getName());
                    if(upd.getIssuer() != null) entity.setIssuer(upd.getIssuer());
                    if(upd.getIssueDate() != null) entity.setIssueDate(upd.getIssueDate());
                    if(upd.getExpirationDate() != null) entity.setExpirationDate(upd.getExpirationDate());
                },
                Certification::getId,
                "Certification"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }

    @DeleteMapping("/certifications/{certId}")
    public ResponseEntity<EmployeeDetailsDTO> deleteCertification(
            @PathVariable Long employeeId,
            @PathVariable Long certId) {

        Employee updated = subResourcesService.deleteSubResource(
                employeeId,
                certId,
                Employee::getCertifications,
                "Certification"
        );

        return ResponseEntity.ok(employeeDetailsMapper.toResponse(updated));
    }
}