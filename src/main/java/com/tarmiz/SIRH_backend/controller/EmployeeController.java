package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.entity.PersonInCharge;
import com.tarmiz.SIRH_backend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(
        name = "Employees",
        description = "Employee management operations"
)
@RestController
@RequestMapping("/hrEmployees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee's details by ID",
            description = "Retrieve detailed information of an employee using their unique ID without contract information "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDetailsDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeDetails(
            @Parameter(description = "ID of the employee to retrieve", example = "1",required = true)
            @PathVariable Long id) {
        EmployeeDetailsDTO dto = employeeService.getEmployeeDetails(id);
        return ResponseEntity.ok(dto);
    }


    @Operation(
            summary = "Get paginated employees list",
            description = "Returns a paginated list of employees with optional sorting"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Employees list retrieved successfully"
    )
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


    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee with basic information only (no validation or exception handling)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Employee created successfully"
    )
    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeCreateDTO dto) {
        Map<String, Object> response = employeeService.createEmployee(dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an employee's infos partially")
    public ResponseEntity<EmployeeDetailsDTO> patchEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeCreateDTO dto) {
        EmployeeDetailsDTO updatedEmployee = employeeService.patchEmployee(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee and all associated infos (education , experience ...)")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        Map<String, Object> response = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(response);
    }
}