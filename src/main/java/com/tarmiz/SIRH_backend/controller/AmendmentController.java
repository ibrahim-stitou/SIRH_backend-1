package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.enums.Contract.AmendmentStatus;
import com.tarmiz.SIRH_backend.enums.Contract.AmendmentType;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantDetailDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantListDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.CreateAmendmentRequest;
import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
import com.tarmiz.SIRH_backend.service.Amendment.AmendmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/amendments")
@RequiredArgsConstructor
public class AmendmentController {

    private final AmendmentService amendmentService;

    @GetMapping
    public ResponseEntity<ApiListResponse<AvenantListDTO>> getAmendmentsList(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) AmendmentStatus status,
            @RequestParam(required = false) AmendmentType typeModif,
            @RequestParam(required = false) String contractReference,
            @RequestParam(required = false) String objet
    ) {

        ApiListResponse<AvenantListDTO> response =
                amendmentService.getAmendmentsList(
                        start, length, sortBy, sortDir,
                        status, typeModif, contractReference, objet
                );

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<AvenantDetailDTO> getAmendmentDetail(@PathVariable Long id) {
        return ResponseEntity.ok(amendmentService.getAmendmentDetail(id));
    }


    @PostMapping("/contract/{contractId}")
    public ResponseEntity<Long> createDraftAmendment(
            @PathVariable Long contractId,
            @Valid @RequestBody CreateAmendmentRequest request
    ) {
        Long amendmentId = amendmentService.createDraftAmendment(contractId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(amendmentId);
    }

    /**
     * Valider / activer un avenant DRAFT
     * @param amendmentId id de l'avenant à appliquer
     */
    @PostMapping("/{amendmentId}/apply")
    public ResponseEntity<String> applyAmendment(@PathVariable Long amendmentId) {

        try {
            amendmentService.applyAmendment(amendmentId);
            return ResponseEntity.ok("Amendment applied successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @Operation(summary = "Supprimer un avenant DRAFT",
            description = "Supprime un avenant uniquement si son statut est BROUILLON")
    @DeleteMapping("/{amendmentId}")
    public ResponseEntity<Void> deleteDraftAmendment(@PathVariable Long amendmentId) {
        amendmentService.deleteDraftAmendment(amendmentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mettre à jour un avenant DRAFT",
            description = "Met à jour un avenant uniquement si son statut est BROUILLON")
    @PutMapping("/{amendmentId}")
    public ResponseEntity<AvenantDetailDTO> updateDraftAmendment(
            @PathVariable Long amendmentId,
            @RequestBody CreateAmendmentRequest dto) {

        Amendment updated = amendmentService.updateDraftAmendment(amendmentId, dto);
        AvenantDetailDTO detailDTO = amendmentService.getAmendmentDetail(updated.getId());
        return ResponseEntity.ok(detailDTO);
    }
}