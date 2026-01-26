package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.enums.MaritalStatus;
import com.tarmiz.SIRH_backend.exception.BusinessException.*;
import com.tarmiz.SIRH_backend.exception.TechnicalException.TechnicalException;
import com.tarmiz.SIRH_backend.mapper.EmployeeCreateMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeesListMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.Address;
import com.tarmiz.SIRH_backend.model.entity.Department;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.entity.PersonInCharge;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import com.tarmiz.SIRH_backend.model.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    // ------------------------- GET EMPLOYEE -------------------------
    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeDetails(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeDetailsMapper.toResponse(employee);
    }

    // ------------------------- LIST EMPLOYEES -------------------------
    public Map<String, Object> getEmployeesList(int start, int length, String sortBy, String sortDir) {
        int page = start / length;
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        sortBy = (sortBy == null || sortBy.isEmpty()) ? "createdAt" : sortBy;

        PageRequest pageRequest = PageRequest.of(page, length, direction, sortBy);
        Page<Employee> pageResult;
        try {
            pageResult = employeeRepository.findAll(pageRequest);
        } catch (DataAccessException ex) {
            throw new TechnicalException("Database error while retrieving employees list", 500, ex);
        }

        List<EmployeesListDTO> data = pageResult.getContent()
                .stream()
                .map(employeesListMapper::toListItem)
                .toList();

        return Map.of(
                "status", "success",
                "message", "Liste des employés récupérée avec succès",
                "data", data,
                "recordsTotal", pageResult.getTotalElements(),
                "recordsFiltered", pageResult.getTotalElements()
        );
    }

    // ------------------------- CREATE EMPLOYEE -------------------------
    @Transactional
    public Map<String, Object> createEmployee(EmployeeCreateDTO dto) {

        Employee employee = employeeCreateMapper.toEntity(dto);

        Department department = departmentRepository.findById(dto.getDepartmentId() != null ? dto.getDepartmentId() : 1L)
                .orElseThrow(() -> new DepartmentNotFoundException(dto.getDepartmentId() != null ? dto.getDepartmentId() : 1L));

        employee.setDepartment(department);

        if (employee.getAddress() != null) {
            employee.getAddress().setEmployee(employee);
        }

        employee.setStatus(dto.getIsActive() != null && dto.getIsActive() ? EmployeeStatus.ACTIF : EmployeeStatus.SUSPENDU);

        try {
            Employee saved = employeeRepository.save(employee);

            EmployeeCreateDTO response = mapEmployeeToDTO(saved);

            return Map.of(
                    "status", "success",
                    "message", "Création réussie",
                    "data", response
            );
        } catch (DataIntegrityViolationException | TransactionSystemException ex) {
            throw new TechnicalException("Database error while creating employee", 500, ex);
        }
    }

    // ------------------------- Create EmergencyContact -------------------------
    @Transactional
    public EmployeeDetailsDTO addEmergencyContact(Long employeeId, PersonInCharge contact) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        contact.setEmployee(employee);
        employee.getEmergencyContacts().add(contact);

        Employee saved = employeeRepository.save(employee);

        return employeeDetailsMapper.toResponse(saved);
    }

    // ------------------------- PATCH EMPLOYEE -------------------------
    @Transactional
    public EmployeeDetailsDTO patchEmployee(Long id, EmployeeCreateDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        try {
            updateEmployeeFields(employee, dto);

            Employee updated = employeeRepository.save(employee);

            return employeeDetailsMapper.toResponse(updated);

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
        if (dto.getFirstName() != null) employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) employee.setLastName(dto.getLastName());
        if (dto.getFirstNameAr() != null) employee.setFirstNameAr(dto.getFirstNameAr());
        if (dto.getLastNameAr() != null) employee.setLastNameAr(dto.getLastNameAr());
        if (dto.getMatricule() != null) employee.setMatricule(dto.getMatricule());
        if (dto.getCin() != null) employee.setCin(dto.getCin());
        if (dto.getBirthDate() != null) employee.setBirthDate(dto.getBirthDate());
        if (dto.getBirthPlace() != null) employee.setBirthPlace(dto.getBirthPlace());
        if (dto.getNationality() != null) employee.setNationality(dto.getNationality());
        if (dto.getGender() != null) employee.setGender(Gender.valueOf(dto.getGender()));
        if (dto.getMaritalStatus() != null) employee.setMaritalStatus(MaritalStatus.valueOf(dto.getMaritalStatus()));
        if (dto.getNumberOfChildren() != null) employee.setNumberOfChildren(dto.getNumberOfChildren());
        if (dto.getPhone() != null) employee.setPhone(dto.getPhone());
        if (dto.getEmail() != null) employee.setEmail(dto.getEmail());

        // Address
        Address address = employee.getAddress();
        if (address == null) {
            address = new Address();
            address.setEmployee(employee);
            employee.setAddress(address);
        }
        if (dto.getAddress() != null) address.setStreet(dto.getAddress());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getPostalCode() != null) address.setPostalCode(dto.getPostalCode());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());

        // Department
        if (dto.getDepartmentId() != null) {
            Department dept = new Department();
            dept.setId(dto.getDepartmentId());
            employee.setDepartment(dept);
        }

        // Status
        if (dto.getIsActive() != null) {
            employee.setStatus(dto.getIsActive() ? EmployeeStatus.ACTIF : EmployeeStatus.SUSPENDU);
        } else if (dto.getStatus() != null) {
            employee.setStatus(EmployeeStatus.valueOf(dto.getStatus().toUpperCase()));
        }
    }

    private EmployeeCreateDTO mapEmployeeToDTO(Employee saved) {
        EmployeeCreateDTO response = new EmployeeCreateDTO();
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setFirstNameAr(saved.getFirstNameAr());
        response.setLastNameAr(saved.getLastNameAr());
        response.setMatricule(saved.getMatricule());
        response.setCin(saved.getCin());
        response.setBirthDate(saved.getBirthDate());
        response.setBirthPlace(saved.getBirthPlace());
        response.setNationality(saved.getNationality());
        response.setGender(saved.getGender() != null ? saved.getGender().name() : null);
        response.setMaritalStatus(saved.getMaritalStatus() != null ? saved.getMaritalStatus().name() : null);
        response.setNumberOfChildren(saved.getNumberOfChildren());
        response.setAddress(saved.getAddress() != null ? saved.getAddress().getStreet() : null);
        response.setCity(saved.getAddress() != null ? saved.getAddress().getCity() : null);
        response.setPostalCode(saved.getAddress() != null ? saved.getAddress().getPostalCode() : null);
        response.setCountry(saved.getAddress() != null ? saved.getAddress().getCountry() : null);
        response.setPhone(saved.getPhone());
        response.setEmail(saved.getEmail());
        response.setDepartmentId(saved.getDepartment() != null ? saved.getDepartment().getId() : null);
        response.setStatus(saved.getStatus() != null ? saved.getStatus().getLabel() : null);
        response.setIsActive(saved.getStatus() == EmployeeStatus.ACTIF);
        return response;
    }
}