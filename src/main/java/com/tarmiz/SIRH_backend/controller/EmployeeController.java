package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hrEmployees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee details by ID",
            description = "Retrieve detailed information of an employee using their unique ID"
    )
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeDetails(
            @Parameter(description = "ID of the employee to retrieve", required = true)
            @PathVariable Long id) {
        EmployeeDetailsDTO dto = employeeService.getEmployeeDetails(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEmployeesList(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Map<String, Object> response = employeeService.getEmployeesList(start, length, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }
}