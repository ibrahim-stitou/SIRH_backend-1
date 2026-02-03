package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationRequestDTO;
import com.tarmiz.SIRH_backend.model.entity.Attestation.Attestation;
import com.tarmiz.SIRH_backend.model.entity.Attestation.DemandeAttestation;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.service.FileService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AttestationMapper {

    @Autowired
    protected FileService fileService;

    /* ================= Mapping DemandeAttestation + Attestation → AttestationDTO ================= */
    @Mapping(target = "employeeId", expression = "java(demande.getEmployee().getId())")
    @Mapping(target = "typeAttestation", expression = "java(demande.getTypeAttestation().getLabel())")
    @Mapping(target = "status", expression = "java(demande.getStatus().getLabel())")
    @Mapping(target = "raison", source = "note")
    @Mapping(target = "notes", expression = "java(demande.getAttestation() != null ? demande.getAttestation().getNotes() : null)")
    @Mapping(target = "dateGeneration", expression = "java(demande.getAttestation() != null ? demande.getAttestation().getDateGeneration() : null)")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "dateRequest", source = "dateRequest")
    @Mapping(target = "dateSouhaitee", source = "dateSouhaitee")
    @Mapping(target = "dateValidation", source = "dateValidation")
    public abstract AttestationDTO toAttestationDTO(DemandeAttestation demande);

    public abstract List<AttestationDTO> toAttestationDTOList(List<DemandeAttestation> demandes);

    /* ================= Mapping Attestation → AttestationRequestDTO ================= */
    @Mapping(target = "requestId", source = "demandeAttestation.id")
    @Mapping(target = "employeeId", expression = "java(attestation.getDemandeAttestation().getEmployee().getId())")
    @Mapping(target = "typeAttestation", expression = "java(attestation.getTypeAttestation().getLabel())")
    @Mapping(target = "documentPath", expression = "java(getDocumentPath(attestation.getId()))")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "dateGeneration", source = "dateGeneration")
    @Mapping(target = "numeroAttestation", source = "numeroAttestation")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    public abstract AttestationRequestDTO toRequestDTO(Attestation attestation);

    public abstract List<AttestationRequestDTO> toRequestDTOList(List<Attestation> attestations);

    /* ================= Helper to fetch documentPath from FileService ================= */
    protected String getDocumentPath(Long attestationId) {
        return fileService.findByEntityAndPurpose(EntityType.ATTESTATION, attestationId, FilePurpose.OTHER)
                .stream()
                .findFirst()
                .map(f -> f.getStoragePath())
                .orElse(null);
    }
}