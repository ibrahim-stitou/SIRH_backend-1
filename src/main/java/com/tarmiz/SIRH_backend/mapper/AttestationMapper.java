package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationRequestDTO;
import com.tarmiz.SIRH_backend.model.entity.Attestation.Attestation;
import com.tarmiz.SIRH_backend.model.entity.Attestation.DemandeAttestation;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.service.document.FileService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttestationMapper {

    // DemandeAttestation -> AttestationDTO
    @Mapping(target = "employeeId", expression = "java(demande.getEmployee().getId())")
    @Mapping(target = "typeAttestation", expression = "java(demande.getTypeAttestation().getLabel())")
    @Mapping(target = "status", expression = "java(demande.getStatus().getLabel())")
    @Mapping(target = "raison", source = "note")
    @Mapping(target = "notes", expression = "java(demande.getAttestation() != null ? demande.getAttestation().getNotes() : null)")
    @Mapping(target = "dateGeneration", expression = "java(demande.getAttestation() != null ? demande.getAttestation().getDateGeneration() : null)")
    AttestationDTO toAttestationDTO(DemandeAttestation demande);

    List<AttestationDTO> toAttestationDTOList(List<DemandeAttestation> demandes);

    // Attestation -> AttestationRequestDTO
    @Mapping(target = "requestId", source = "demandeAttestation.id")
    @Mapping(target = "employeeId", expression = "java(attestation.getDemandeAttestation().getEmployee().getId())")
    @Mapping(target = "typeAttestation", expression = "java(attestation.getTypeAttestation().getLabel())")
    @Mapping(target = "documentPath", expression = "java(getDocumentPath(attestation.getId(), fileService))")
    AttestationRequestDTO toRequestDTO(Attestation attestation, @Context FileService fileService);

    List<AttestationRequestDTO> toRequestDTOList(List<Attestation> attestations, @Context FileService fileService);

    // Helper method
    default String getDocumentPath(Long attestationId, @Context FileService fileService) {
        return fileService.findByEntityAndPurpose(EntityType.ATTESTATION, attestationId, FilePurpose.OTHER)
                .stream()
                .findFirst()
                .map(f -> f.getStoragePath())
                .orElse(null);
    }
}