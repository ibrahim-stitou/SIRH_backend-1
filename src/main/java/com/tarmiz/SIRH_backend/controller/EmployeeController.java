package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeIdNameDTO;
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

import java.util.List;
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
            summary = "Get list of employees with ID and full name",
            description = "Returns a list of employees containing only their ID and full name (firstName + lastName)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                {
                                  "status": "success",
                                  "message": "Liste des employés récupérée avec succès",
                                  "data": [
                                    {"id": 1, "fullName": "Ahmed El Amrani"},
                                    {"id": 2, "fullName": "Fatima Zahra"}
                                  ]
                                }
                                """
                            )
                    )
            )
    })
    @GetMapping("/id-name")
    public ResponseEntity<Map<String, Object>> getEmployeesIdAndFullName() {
        List<EmployeeIdNameDTO> data = employeeService.getEmployeesIdAndFullName();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Liste des employés récupérée avec succès",
                "data", data
        ));
    }


    // ------------------------- LIST EMPLOYEES -------------------------

    @Operation(
            summary = "Get paginated list of employees",
            description = "Returns a paginated list of employees using offset-based pagination and optional sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    description = "Paginated employees response",
                                    example = """
                                    {
                                      "status": "success",
                                      "message": "Liste des employés récupérée avec succès",
                                      "data": [],
                                      "recordsTotal": 25,
                                      "recordsFiltered": 25
                                    }
                                    """
                            )
                    )
            )
    })
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                    {
                                      "status": "success",
                                      "message": "Création réussie",
                                      "data": {}
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Employee creation payload",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = EmployeeCreateDTO.class)
            )
    )
    public ResponseEntity<Map<String, Object>> createEmployee(
            @Valid @org.springframework.web.bind.annotation.RequestBody EmployeeCreateDTO dto
    ) {
        Map<String, Object> response = employeeService.createEmployee(dto);
        return ResponseEntity.ok(response);
    }

    // ------------------------- PATCH EMPLOYEE -------------------------

    @Operation(
            summary = "Partially update an employee",
            description = "Updates only the provided fields of an existing employee"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
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
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDetailsDTO> patchEmployee(
            @Parameter(description = "Employee unique identifier", required = true, example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fields to update (partial)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EmployeeCreateDTO.class)
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody EmployeeCreateDTO dto
    ) {
        EmployeeDetailsDTO updatedEmployee = employeeService.patchEmployee(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    // ------------------------- DELETE EMPLOYEE -------------------------
    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee and all associated information (education, experience, etc.)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                    {
                                      "status": "success",
                                      "message": "Suppression réussie"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        Map<String, Object> response = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(response);
    }
}