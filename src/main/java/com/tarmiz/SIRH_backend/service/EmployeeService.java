package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.exception.EmployeeNotFoundException;
import com.tarmiz.SIRH_backend.mapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeesListMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsMapper employeeDetailsMapper;
    private final EmployeesListMapper employeesListMapper;

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
}