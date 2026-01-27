package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.*;
import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.service.AttestationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(
        name = "Attestations",
        description = "Gestion des attestations"
)
@RestController
@RequestMapping("/")
public class AttestationController {

    @Autowired
    private AttestationService service;

    @Autowired
    private AttestationService attestationService;

    /* ================== Attestation Requests ================== */

    @GetMapping("/attestationRequests")
    @Operation(summary = "List attestation requests with optional filters and pagination")
    public ApiListResponse<AttestationDTO> listRequests(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) AttestationType type,
            @RequestParam(required = false) AttestationDemandStatus status,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return service.listRequests(employeeId, type, status, start, length, sortDir);
    }

    @PostMapping("/attestationRequests")
    @Operation(summary = "Create a new attestation request")
    public AttestationDTO createRequest(
            @RequestBody AttestationRequestCreationDTO request
    ) {
        return service.createRequest(
                request.getEmployeeId(),
                request.getType(),
                request.getDateSouhaitee(),
                request.getNote()
        );
    }

    @GetMapping("/attestationRequests/{id}")
    @Operation(summary = "Get attestation request details")
    public AttestationDTO getRequest(@PathVariable Long id) {
        return service.getRequest(id);
    }

    @PutMapping("/attestationRequests/{id}")
    @Operation(summary = "Update attestation request status")
    public AttestationDTO updateRequestStatus(@PathVariable Long id, @RequestParam AttestationDemandStatus status) {
        return service.updateRequestStatus(id, status);
    }

    @DeleteMapping("/attestationRequests/{id}")
    @Operation(summary = "Cancel attestation request (soft delete)")
    public ApiResponse cancelRequest(@PathVariable Long id) {
        service.cancelRequest(id);
        return ApiResponse.success("Attestation request canceled successfully");
    }

    /* ================== Attestations ================== */

    @GetMapping("/attestations")
    @Operation(summary = "List generated attestations with optional filters")
    public ApiListResponse<AttestationRequestDTO> listAttestations(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) AttestationType type,
            @RequestParam(required = false) String numero,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return service.listAttestations(employeeId, type, numero, start, length, sortDir);
    }

    @GetMapping("/attestations/{id}")
    @Operation(summary = "Get attestation details")
    public AttestationRequestDTO getAttestation(@PathVariable Long id) {
        return service.getAttestation(id);
    }

    @PostMapping("/attestations/{requestId}")
    @Operation(summary = "Generate PDF for an attestation from template")
    public ApiResponse generateAttestationPdf(
            @PathVariable Long requestId
    ) {
        attestationService.generateAttestation(requestId);

        return ApiResponse.success("Attestation generated successfully");
    }

    @PutMapping("/attestations/{id}")
    @Operation(summary = "Update attestation metadata")
    public AttestationRequestDTO updateAttestation(
            @PathVariable Long id,
            @RequestParam(required = false) AttestationType type,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) LocalDateTime dateGeneration,
            @RequestParam(required = false) String notes
    ) {
        return service.updateAttestation(id, type, numero, dateGeneration, notes);
    }
}