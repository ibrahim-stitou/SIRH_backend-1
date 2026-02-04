package com.tarmiz.SIRH_backend.controller.Settings.RessourcesHumaines;

import com.tarmiz.SIRH_backend.model.DTO.ApiResponseDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentListDTO;
import com.tarmiz.SIRH_backend.model.DTO.DepartmentCreationDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/departements")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Get all departments", description = "Retrieve a list of all departments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<DepartmentListDTO>>> list() {
        List<DepartmentListDTO> departments = departmentService.listAll();
        return ResponseEntity.ok(ApiResponseDTO.success("Departments retrieved successfully", departments));
    }

    @Operation(summary = "Create a new department", description = "Create a department with name, description, and status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation or business error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<DepartmentCreationDTO>> create(@RequestBody DepartmentCreationDTO department) {
        DepartmentCreationDTO saved = departmentService.create(department);
        return ResponseEntity.ok(ApiResponseDTO.success("Department created successfully", saved));
    }

    @Operation(summary = "Get a department by ID", description = "Retrieve a single department by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Department>> show(@PathVariable Long id) {
        Department department = departmentService.getById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Department retrieved successfully", department));
    }

    @Operation(summary = "Update a department", description = "Update department's name, description or status")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Department>> update(@PathVariable Long id, @RequestBody Department department) {
        Department updated = departmentService.update(id, department);
        return ResponseEntity.ok(ApiResponseDTO.success("Department updated successfully", updated));
    }

    @Operation(summary = "Delete a department", description = "Delete department if inactive and has no employees")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Department deleted successfully", null));
    }

    @Operation(summary = "Activate a department", description = "Set department status to ACTIVE")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO<DepartmentListDTO>> activate(@PathVariable Long id) {
        DepartmentListDTO dept = departmentService.activate(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Department activated successfully", dept));
    }

    @Operation(summary = "Deactivate a department", description = "Set department status to INACTIVE")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<DepartmentListDTO>> deactivate(@PathVariable Long id) {
        DepartmentListDTO dept = departmentService.deactivate(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Department deactivated successfully", dept));
    }
}
