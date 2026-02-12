package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.*;
import com.tarmiz.SIRH_backend.exception.BusinessException.*;
import com.tarmiz.SIRH_backend.exception.TechnicalException.TechnicalException;
import com.tarmiz.SIRH_backend.mapper.EmployeeMapper.EmployeeCreateMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeeMapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeeMapper.EmployeesListMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeIdNameDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.PersonInCharge;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.GroupRepository;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.DepartmentRepository;
import com.tarmiz.SIRH_backend.specs.EmployeeSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final EmployeeDetailsMapper employeeDetailsMapper;
    private final EmployeesListMapper employeesListMapper;
    private final EmployeeCreateMapper employeeCreateMapper;

    private final GroupRepository groupRepository ;

    // ------------------------- GET EMPLOYEE -------------------------
    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeDetails(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeDetailsMapper.toDto(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeIdNameDTO> getEmployeesIdAndFullName() {
        return employeeRepository.findAll()
                .stream()
                .map(emp -> new EmployeeIdNameDTO(
                        emp.getId(),
                        emp.getFirstName() + " " + emp.getLastName()
                ))
                .toList();
    }

    // ------------------------- LIST EMPLOYEES -------------------------
    public ApiListResponse<EmployeesListDTO> getEmployeesList(
            int start,
            int length,
            String sortBy,
            String sortDir,
            String name,
            String matricule,
            String contractType,
            String status
    ) {
        int page = start / length;
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        sortBy = (sortBy == null || sortBy.isEmpty()) ? "createdDate" : sortBy;
        PageRequest pageRequest = PageRequest.of(page, length, direction, sortBy);

        // Build combined Specification
        Specification<Employee> spec = Specification.where(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasMatricule(matricule))
                .and(EmployeeSpecifications.hasStatus(status))
                .and(EmployeeSpecifications.hasContractType(contractType));

        Page<Employee> pageResult = employeeRepository.findAll(spec, pageRequest);

        List<EmployeesListDTO> data = pageResult.getContent()
                .stream()
                .map(employeesListMapper::toListItem)
                .toList();

        return new ApiListResponse<>(
                "success",
                "Liste des employés récupérée avec succès",
                data,
                pageResult.getTotalElements(),
                pageResult.getTotalElements()
        );
    }

    // ------------------------- CREATE EMPLOYEE -------------------------
    @Transactional
    public EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto) {

        // =========================
        // 1️⃣ Mapping DTO -> Entity
        // =========================
        Employee employee = employeeCreateMapper.toEntity(dto);

        // =========================
        // 2️⃣ Status (default ACTIF)
        // =========================
        EmployeeStatus status = (dto.getStatus() == null || dto.getStatus().isBlank())
                ? EmployeeStatus.ACTIF
                : EmployeeStatus.valueOf(dto.getStatus());

        employee.setStatus(status);

        // =========================
        // 3️⃣ Enum conversions
        // =========================
        employee.setGender(Gender.valueOf(dto.getGender()));
        employee.setMaritalStatus(MaritalStatus.valueOf(dto.getMaritalStatus()));
        employee.setNationality(Nationality.valueOf(dto.getNationality()));

        // =========================
        // 4️⃣ Department validation
        // =========================
        if (status == EmployeeStatus.ACTIF) {

            if (dto.getDepartmentId() == null) {
                throw new IllegalArgumentException(
                        "Department is mandatory for ACTIVE employees"
                );
            }

            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));

            if (department.getStatus() != DepartmentStatus.ACTIVE) {
                throw new IllegalStateException(
                        "Cannot assign employee to inactive department"
                );
            }

            employee.setDepartment(department);
        }

        // =========================
        // 5️⃣ Group (OPTIONAL)
        // =========================
        if (dto.getGroupId() != null) {

            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found"));

            employee.setGroup(group);
        }

        // =========================
        // 6️⃣ Link children to parent
        // =========================
        if (employee.getAddress() != null) {
            employee.getAddress().setEmployee(employee);
        }

        if (employee.getSkills() != null) {
            employee.getSkills().forEach(s -> s.setEmployee(employee));
        }

        if (employee.getEducationList() != null) {
            employee.getEducationList().forEach(e -> e.setEmployee(employee));
        }

        if (employee.getExperiences() != null) {
            employee.getExperiences().forEach(e -> e.setEmployee(employee));
        }

        if (employee.getCertifications() != null) {
            employee.getCertifications().forEach(c -> c.setEmployee(employee));
        }

        if (employee.getEmergencyContacts() != null) {
            employee.getEmergencyContacts().forEach(p -> p.setEmployee(employee));
        }

        // =========================
        // 7️⃣ Persist
        // =========================
        Employee saved = employeeRepository.save(employee);

        // =========================
        // 8️⃣ Return details DTO
        // =========================
        return employeeDetailsMapper.toDto(saved);
    }

    // ------------------------- Create EmergencyContact -------------------------
    @Transactional
    public EmployeeDetailsDTO addEmergencyContact(Long employeeId, PersonInCharge contact) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        contact.setEmployee(employee);
        employee.getEmergencyContacts().add(contact);

        Employee saved = employeeRepository.save(employee);

        return employeeDetailsMapper.toDto(saved);
    }

    // ------------------------- PATCH EMPLOYEE -------------------------
    @Transactional
    public EmployeeDetailsDTO patchEmployee(Long id, EmployeeCreateDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        try {
            updateEmployeeFields(employee, dto);

            Employee updated = employeeRepository.save(employee);

            return employeeDetailsMapper.toDto(updated);

        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (DataAccessException | TransactionSystemException ex) {
            throw new TechnicalException("Database error while updating employee", 500, ex);
        }
    }

    // ------------------------- DELETE EMPLOYEE -------------------------
    @Transactional
    public Map<String, Object> deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        try {
            employeeRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new EmployeeDeletionNotAllowedException(id);
        } catch (DataAccessException | TransactionSystemException ex) {
            throw new TechnicalException("Database error while deleting employee", 500, ex);
        }

        return Map.of(
                "status", "success",
                "message", "Suppression réussie"
        );
    }

    // ------------------------- HELPER METHODS -------------------------
    private void updateEmployeeFields(Employee employee, EmployeeCreateDTO dto) {

        // ======== Basic fields ========
        if (dto.getFirstName() != null) employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) employee.setLastName(dto.getLastName());
        if (dto.getFirstNameAr() != null) employee.setFirstNameAr(dto.getFirstNameAr());
        if (dto.getLastNameAr() != null) employee.setLastNameAr(dto.getLastNameAr());
        if (dto.getMatricule() != null) employee.setMatricule(dto.getMatricule());
        if (dto.getCin() != null) employee.setCin(dto.getCin());
        if (dto.getBirthDate() != null) employee.setBirthDate(dto.getBirthDate());
        if (dto.getBirthPlace() != null) employee.setBirthPlace(dto.getBirthPlace());
        if (dto.getNationality() != null) employee.setNationality(Nationality.valueOf(dto.getNationality()));
        if (dto.getGender() != null) employee.setGender(Gender.valueOf(dto.getGender()));
        if (dto.getMaritalStatus() != null) employee.setMaritalStatus(MaritalStatus.valueOf(dto.getMaritalStatus()));
        if (dto.getNumberOfChildren() != null) employee.setNumberOfChildren(dto.getNumberOfChildren());
        if (dto.getPhone() != null) employee.setPhone(dto.getPhone());
        if (dto.getEmail() != null) employee.setEmail(dto.getEmail());

        // ======== Status ========
        if (dto.getStatus() != null) {
            employee.setStatus(EmployeeStatus.valueOf(dto.getStatus().toUpperCase()));
        } else if (employee.getStatus() == null) {
            employee.setStatus(EmployeeStatus.ACTIF);
        }

        // ======== Department (mandatory) ========
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            if (dept.getStatus() != DepartmentStatus.ACTIVE) {
                throw new IllegalStateException("Cannot assign employee to inactive department");
            }
            employee.setDepartment(dept);
        }

        // ======== Group (optional) ========
        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found"));
            employee.setGroup(group);
        }

        // ======== Nested resources ========
        // Using your mappers instead of manually creating
        if (dto.getAddress() != null) {
            employee.setAddress(employeeCreateMapper.toEntity(dto.getAddress()));
            employee.getAddress().setEmployee(employee); // ensure bi-directional link
        }

        if (dto.getEmergencyContacts() != null) {
            employee.setEmergencyContacts(
                    dto.getEmergencyContacts().stream()
                            .map(employeeCreateMapper::toEntity)
                            .peek(c -> c.setEmployee(employee))
                            .toList()
            );
        }

        if (dto.getSkills() != null) {
            employee.setSkills(
                    dto.getSkills().stream()
                            .map(employeeCreateMapper::toEntity)
                            .peek(s -> s.setEmployee(employee))
                            .toList()
            );
        }

        if (dto.getEducationList() != null) {
            employee.setEducationList(
                    dto.getEducationList().stream()
                            .map(employeeCreateMapper::toEntity)
                            .peek(e -> e.setEmployee(employee))
                            .toList()
            );
        }

        if (dto.getExperiences() != null) {
            employee.setExperiences(
                    dto.getExperiences().stream()
                            .map(employeeCreateMapper::toEntity)
                            .peek(e -> e.setEmployee(employee))
                            .toList()
            );
        }

        if (dto.getCertifications() != null) {
            employee.setCertifications(
                    dto.getCertifications().stream()
                            .map(employeeCreateMapper::toEntity)
                            .peek(c -> c.setEmployee(employee))
                            .toList()
            );
        }
    }
}