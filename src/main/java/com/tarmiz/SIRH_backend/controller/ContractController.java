package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.enums.Contract.ContractStatusEnum;
import com.tarmiz.SIRH_backend.enums.Contract.ContractTypeEnum;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.ApiResponseDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantDetailDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractCreationDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import com.tarmiz.SIRH_backend.service.Amendment.AmendmentService;
import com.tarmiz.SIRH_backend.service.ContractService;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.EmploiService;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.MetierService;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.PosteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contracts")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Contracts", description = "Endpoints for contracts, emplois, and postes info")
public class ContractController {

    private final EmploiService emploiService;
    private final PosteService posteService;
    private final MetierService metierService;
    private final ContractService contractService;
    private final AmendmentService amendmentService;

    // ------------------------- CONTRACTS -------------------------

    @Operation(summary = "Get paginated list of contracts", description = "Return paginated list of contracts with details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contracts retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiListResponse<ContractListDTO>> getContractsPaginated(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) ContractTypeEnum type,
            @RequestParam(required = false) ContractStatusEnum status,
            @RequestParam(required = false) Long departmentId
    ) {

        ApiListResponse<ContractListDTO> response =
                contractService.getAllContracts(
                        start, length, sortDir,
                        employeeId, type, status, departmentId
                );

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get full details of a contract by ID", description = "Retrieve contract details including job, clauses, salary, schedule, and trial period")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ContractDetailsDTO>> getContractDetails(
            @Parameter(description = "Contract ID", required = true) @PathVariable Long id) {

        ContractDetailsDTO dto = contractService.getContractDetails(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract details retrieved successfully", dto));
    }

    @Operation(summary = "Create a new contract", description = "Creates a new contract with associated job, clauses, salary, schedule, and trial period if provided")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contract created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or missing required fields"),
            @ApiResponse(responseCode = "404", description = "Employee, Poste, or Clause not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ContractDetailsDTO>> createContract(
            @RequestBody ContractCreationDTO dto) throws Exception {

        ContractDetailsDTO createdContract = contractService.createContract(dto);
        return new ResponseEntity<>(ApiResponseDTO.success("Contract created successfully", createdContract), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a contract partially", description = "Updates provided fields of an existing contract")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract updated successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found"),
            @ApiResponse(responseCode = "400", description = "Cannot update contract in current status")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> update(
            @Parameter(description = "Contract ID", required = true) @PathVariable Long id,
            @RequestBody ContractCreationDTO dto) {

        contractService.updateContract(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract updated successfully", null));
    }

    @Operation(summary = "Validate a contract", description = "Mark a contract as active after all mandatory fields are completed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract validated successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found"),
            @ApiResponse(responseCode = "400", description = "Contract is incomplete and cannot be validated")
    })
    @PatchMapping("/{id}/validate")
    public ResponseEntity<ApiResponseDTO<Void>> validate(
            @Parameter(description = "Contract ID", required = true) @PathVariable Long id) {

        contractService.validateContract(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract validated successfully", null));
    }

    @Operation(summary = "Cancel a contract", description = "Mark a contract as suspended")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDTO<Void>> cancel(
            @Parameter(description = "Contract ID", required = true) @PathVariable Long id) {

        contractService.cancelContract(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract canceled successfully", null));
    }

    @Operation(summary = "Delete a contract in draft", description = "Deletes a contract only if it is in draft status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete contract in current status"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteContract(
            @Parameter(description = "Contract ID", required = true) @PathVariable Long id) {

        contractService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract deleted successfully", null));
    }

    @Operation(summary = "Upload a signed contract PDF", description = "Upload a scanned signed contract file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Signed contract uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    @PostMapping("/{id}/upload-signed")
    public ResponseEntity<ApiResponseDTO<File>> uploadSigned(
            @Parameter(description = "Contract ID", required = true) @PathVariable("id") Long contractId,
            @Parameter(description = "Signed contract file", required = true) @RequestParam("file") MultipartFile file) {

        File uploadedFile = contractService.uploadSignedContract(contractId, file);
        return ResponseEntity.ok(ApiResponseDTO.success("Signed contract uploaded successfully", uploadedFile));
    }

    // ------------------------- METIERS, EMPLOIS, POSTES -------------------------

    @Operation(summary = "List all metiers (id + libelle)", description = "Return all metiers simplified")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Metiers retrieved successfully")})
    @GetMapping("/metiers")
    public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> getAllMetiers() {
        List<Map<String, Object>> metiers = metierService.getAllMetiersSimplified();
        return ResponseEntity.ok(ApiResponseDTO.success("Metiers retrieved successfully", metiers));
    }

    @Operation(summary = "List emplois by metier ID", description = "Return list of emplois for a given metier")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Emplois retrieved successfully")})
    @GetMapping("/metier/{metierId}/emplois")
    public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> getEmploisByMetier(
            @Parameter(description = "Metier ID", required = true) @PathVariable Long metierId) {

        List<Map<String, Object>> emplois = emploiService.getEmploisByMetier(metierId);
        return ResponseEntity.ok(ApiResponseDTO.success("Emplois retrieved successfully", emplois));
    }

    @Operation(summary = "List postes by emploi ID", description = "Return list of postes for a given emploi")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Postes retrieved successfully")})
    @GetMapping("/emploi/{emploiId}/postes")
    public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> getPostesByEmploi(
            @Parameter(description = "Emploi ID", required = true) @PathVariable Long emploiId) {

        List<Map<String, Object>> postes = posteService.getPostesByEmploi(emploiId);
        return ResponseEntity.ok(ApiResponseDTO.success("Postes retrieved successfully", postes));
    }

    @PostMapping("/{id}/generate")
    @Operation(
            summary = "Generate contract PDF",
            description = "Generates the contract PDF for the given contract ID, using all contract details, job, trial period, schedule, and salary."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contract generated successfully"),
            @ApiResponse(responseCode = "404", description = "Contract not found"),
            @ApiResponse(responseCode = "400", description = "Contract status not allowed for generation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseDTO<Void>> generateContract(
            @Parameter(description = "Contract ID", required = true)
            @PathVariable("id") Long contractId) {

        contractService.generateContract(contractId);
        return ResponseEntity.ok(ApiResponseDTO.success("Contract generated successfully", null));
    }


    @Operation(summary = "Récupérer la liste des avenants d'un contrat",
            description = "Retourne tous les avenants liés à un contrat donné")
    @GetMapping("/{contractId}")
    public ResponseEntity<List<AvenantDetailDTO>> getAmendmentsByContract(
            @PathVariable Long contractId) {
        List<AvenantDetailDTO> dtos = amendmentService.getAmendmentsByContractId(contractId);
        return ResponseEntity.ok(dtos);
    }

}