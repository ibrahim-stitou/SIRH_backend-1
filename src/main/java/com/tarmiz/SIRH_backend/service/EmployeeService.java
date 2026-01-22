package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.exception.EmployeeNotFoundException;
import com.tarmiz.SIRH_backend.mapper.EmployeeCreateMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeesListMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.Department;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import com.tarmiz.SIRH_backend.model.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsMapper employeeDetailsMapper;
    private final EmployeesListMapper employeesListMapper;
    private final EmployeeCreateMapper employeeCreateMapper;

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeDetails(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeDetailsMapper.toResponse(employee);
    }

    public Map<String, Object> getEmployeesList(int start, int length, String sortBy, String sortDir) {
        int page = start / length;
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdAt";
        }
        PageRequest pageRequest = PageRequest.of(page, length, direction, sortBy);
        Page<Employee> pageResult = employeeRepository.findAll(pageRequest);

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

    @Transactional
    public Map<String, Object> createEmployee(EmployeeCreateDTO dto) {

        Employee employee = employeeCreateMapper.toEntity(dto);

        Department department = departmentRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Department with id 1 not found"));
        employee.setDepartment(department);
        employee.setCreatedAt(LocalDateTime.now());

        if (employee.getAddress() != null) {
            employee.getAddress().setEmployee(employee);
        }

        employee.setStatus(dto.getIsActive() != null && dto.getIsActive() ? employee.getStatus() : EmployeeStatus.SUSPENDU);

        // Persist Employee (cascade saves Address)
        Employee saved = employeeRepository.save(employee);

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

        return Map.of(
                "status", "success",
                "message", "Création réussie",
                "data", response
        );
    }
}