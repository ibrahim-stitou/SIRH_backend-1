package com.tarmiz.SIRH_backend;

import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.enums.MaritalStatus;
import com.tarmiz.SIRH_backend.exception.BusinessException.*;
import com.tarmiz.SIRH_backend.exception.TechnicalException.TechnicalException;
import com.tarmiz.SIRH_backend.mapper.EmployeeCreateMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeeDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.EmployeesListMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.entity.Department;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.repository.DepartmentRepository;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import com.tarmiz.SIRH_backend.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceExceptionTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeDetailsMapper employeeDetailsMapper;

    @Mock
    private EmployeesListMapper employeesListMapper;

    @Mock
    private EmployeeCreateMapper employeeCreateMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeCreateDTO dto;
    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId(1L);

        employee = new Employee();
        employee.setId(1L);
        employee.setStatus(EmployeeStatus.ACTIF);
        employee.setGender(Gender.Male);
        employee.setMaritalStatus(MaritalStatus.CELIBATAIRE);
        employee.setDepartment(department);

        dto = new EmployeeCreateDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setGender("Male");
        dto.setMaritalStatus("CELIBATAIRE");
        dto.setIsActive(true);
        dto.setDepartmentId(1L);
    }

    // ------------------- getEmployeeDetails -------------------
    @Test
    void getEmployeeDetails_shouldThrowEmployeeNotFoundException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeDetails(1L));
        assertEquals("Employee with id 1 not found", ex.getMessage());
    }

    // ------------------- getEmployeesList -------------------
    @Test
    void getEmployeesList_shouldThrowTechnicalException_onDBError() {
        when(employeeRepository.findAll(any(Pageable.class))).thenThrow(new DataIntegrityViolationException("DB failure"));

        TechnicalException ex = assertThrows(TechnicalException.class,
                () -> employeeService.getEmployeesList(0, 10, null, "asc"));
        assertEquals("Database error while retrieving employees list", ex.getMessage());
    }

    // ------------------- createEmployee -------------------
    @Test
    void createEmployee_shouldThrowDepartmentNotFoundException() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        DepartmentNotFoundException ex = assertThrows(DepartmentNotFoundException.class,
                () -> employeeService.createEmployee(dto));
        assertEquals("Department with id 1 not found", ex.getMessage());
    }

    @Test
    void createEmployee_shouldThrowTechnicalException_onDBError() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeCreateMapper.toEntity(dto)).thenReturn(employee);
        when(employeeRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        TechnicalException ex = assertThrows(TechnicalException.class,
                () -> employeeService.createEmployee(dto));
        assertEquals("Database error while creating employee", ex.getMessage());
    }

    // ------------------- patchEmployee -------------------
    @Test
    void patchEmployee_shouldThrowEmployeeNotFoundException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.patchEmployee(1L, dto));
        assertEquals("Employee with id 1 not found", ex.getMessage());
    }

    @Test
    void patchEmployee_shouldThrowIllegalArgumentException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        dto.setGender("INVALID_GENDER"); // will cause IllegalArgumentException

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.patchEmployee(1L, dto));
        assertTrue(ex.getMessage().contains("No enum constant"));
    }

    @Test
    void patchEmployee_shouldThrowTechnicalException_onDBError() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doThrow(new DataIntegrityViolationException("DB failure")).when(employeeRepository).save(any());

        TechnicalException ex = assertThrows(TechnicalException.class,
                () -> employeeService.patchEmployee(1L, dto));
        assertEquals("Database error while updating employee", ex.getMessage());
    }

    // ------------------- deleteEmployee -------------------
    @Test
    void deleteEmployee_shouldThrowEmployeeNotFoundException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));
        assertEquals("Employee with id 1 not found", ex.getMessage());
    }

    @Test
    void deleteEmployee_shouldThrowEmployeeDeletionNotAllowedException() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(employeeRepository).deleteById(1L);

        EmployeeDeletionNotAllowedException ex = assertThrows(EmployeeDeletionNotAllowedException.class,
                () -> employeeService.deleteEmployee(1L));
        assertEquals("Employee with id 1 cannot be deleted due to existing constraints", ex.getMessage());
    }

    @Test
    void deleteEmployee_shouldThrowTechnicalException_onDBError() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doThrow(TransactionSystemException.class).when(employeeRepository).deleteById(1L);

        TechnicalException ex = assertThrows(TechnicalException.class,
                () -> employeeService.deleteEmployee(1L));
        assertEquals("Database error while deleting employee", ex.getMessage());
    }
}