package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.*;
import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationRequestCreationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationRequestDTO;
import com.tarmiz.SIRH_backend.service.AttestationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(
        name = "Attestations",
        description = "Gestion des attestations et des demandes d'attestation"
)
@RestController
@AllArgsConstructor
@RequestMapping("/")
public class AttestationController {

    private final AttestationService attestationService;

    /* ================== Attestation Requests ================== */

    @Operation(
            summary = "List attestation requests",
            description = "Returns a paginated list of attestation requests with optional filters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Attestation requests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiListResponse.class)
                    )
            )
    })
    @GetMapping("/attestationRequests")
    public ApiListResponse<AttestationDTO> listRequests(
            @Parameter(description = "Employee identifier")
            @RequestParam(required = false) Long employeeId,

            @Parameter(
                    description = "Attestation type",
                    schema = @Schema(implementation = AttestationType.class)
            )
            @RequestParam(required = false) AttestationType type,

            @Parameter(
                    description = "Request status",
                    schema = @Schema(implementation = AttestationDemandStatus.class)
            )
            @RequestParam(required = false) AttestationDemandStatus status,

            @Parameter(description = "Page index", example = "0")
            @RequestParam(defaultValue = "0") int start,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int length,

            @Parameter(
                    description = "Sort direction",
                    example = "desc",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return attestationService.listRequests(employeeId, type, status, start, length, sortDir);
    }

    @PostMapping("/attestationRequests")
    @Operation(summary = "Create a new attestation request")
    public AttestationDTO createRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Attestation request creation payload",
                    content = @Content(
                            schema = @Schema(implementation = AttestationRequestCreationDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody AttestationRequestCreationDTO request
    ) {
        return attestationService.createRequest(
                request.getEmployeeId(),
                request.getType(),
                request.getDateSouhaitee(),
                request.getNote()
        );
    }

    @Operation(
            summary = "Get attestation request details"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestation request retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttestationDTO.class)
            )
    )
    @GetMapping("/attestationRequests/{id}")
    public AttestationDTO getRequest(
            @Parameter(description = "Attestation request identifier", required = true, example = "1")
            @PathVariable Long id
    ) {
        return attestationService.getRequest(id);
    }

    @Operation(
            summary = "Update attestation request status"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestation request status updated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttestationDTO.class)
            )
    )
    @PutMapping("/attestationRequests/{id}")
    public AttestationDTO updateRequestStatus(
            @Parameter(description = "Attestation request identifier", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(
                    description = "New request status",
                    required = true,
                    schema = @Schema(implementation = AttestationDemandStatus.class)
            )
            @RequestParam AttestationDemandStatus status
    ) {
        return attestationService.updateRequestStatus(id, status);
    }

    @Operation(
            summary = "Cancel attestation request",
            description = "Soft delete: sets request status to CANCELED"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestation request canceled successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)
            )
    )
    @DeleteMapping("/attestationRequests/{id}")
    public ApiResponseDTO<Void> cancelRequest(@PathVariable Long id) {
        attestationService.deleteRequest(id);
        return ApiResponseDTO.success("Attestation request canceled successfully", null);
    }

    /* ================== Attestations ================== */

    @Operation(
            summary = "List generated attestations",
            description = "Returns a paginated list of generated attestations with optional filters"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestations retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiListResponse.class)
            )
    )
    @GetMapping("/attestations")
    public ApiListResponse<AttestationRequestDTO> listAttestations(
            @Parameter(description = "Employee identifier")
            @RequestParam(required = false) Long employeeId,

            @Parameter(
                    description = "Attestation type",
                    schema = @Schema(implementation = AttestationType.class)
            )
            @RequestParam(required = false) AttestationType type,

            @Parameter(description = "Attestation number (partial match)")
            @RequestParam(required = false) String numero,

            @Parameter(description = "Page index", example = "0")
            @RequestParam(defaultValue = "0") int start,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int length,

            @Parameter(
                    description = "Sort direction",
                    example = "desc",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return attestationService.listAttestations(employeeId, type, numero, start, length, sortDir);
    }

    @Operation(
            summary = "Get attestation details"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestation retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttestationRequestDTO.class)
            )
    )
    @GetMapping("/attestations/{id}")
    public AttestationRequestDTO getAttestation(@PathVariable Long id) {
        return attestationService.getAttestation(id);
    }

    @Operation(
            summary = "Generate attestation PDF",
            description = "Generates and stores a PDF for an approved attestation request"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attestation generated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)
            )
    )
    @PostMapping("/attestations/{requestId}")
    public ApiResponseDTO<Void> generateAttestationPdf(
            @PathVariable Long requestId
    ) {
        attestationService.generateAttestation(requestId);

        return ApiResponseDTO.success("Attestation generated successfully", null);
    }

    @PutMapping("/attestations/{id}")
    @Operation(summary = "Update attestation metadata")
    public AttestationRequestDTO updateAttestation(

            @Parameter(description = "Attestation identifier", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(schema = @Schema(implementation = AttestationType.class))
            @RequestParam(required = false) AttestationType type,

            @Parameter(description = "Attestation number")
            @RequestParam(required = false) String numero,

            @Parameter(description = "Generation date (ISO-8601)")
            @RequestParam(required = false) LocalDateTime dateGeneration,

            @Parameter(description = "Additional notes")
            @RequestParam(required = false) String notes
    ) {
        return attestationService.updateAttestation(id, type, numero, dateGeneration, notes);
    }
}