package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.mapper.AttestationMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTOs.AttestationRequestDTO;
import com.tarmiz.SIRH_backend.model.entity.Attestation.Attestation;
import com.tarmiz.SIRH_backend.model.entity.Attestation.DemandeAttestation;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.repository.AttestationRepos.AttestationRepository;
import com.tarmiz.SIRH_backend.model.repository.AttestationRepos.DemandeAttestationRepository;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.service.document.FileService;
import com.tarmiz.SIRH_backend.service.document.PdfGeneratorService;
import com.tarmiz.SIRH_backend.specs.AttestationRequestsSpecifications;
import com.tarmiz.SIRH_backend.specs.AttestationSpecifications;
import com.tarmiz.SIRH_backend.util.PdfTemplateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttestationService {

    private final DemandeAttestationRepository demandeRepo;
    private final AttestationRepository attestationRepo;
    private final EmployeeRepository employeeRepo;
    private final PdfGeneratorService pdfGeneratorService;
    private final FileService fileService;
    private final PdfTemplateUtils helper;
    private final AttestationMapper mapper;

    // Constructor injection
    public AttestationService(
            DemandeAttestationRepository demandeRepo,
            AttestationRepository attestationRepo,
            EmployeeRepository employeeRepo,
            PdfGeneratorService pdfGeneratorService,
            FileService fileService,
            PdfTemplateUtils helper,
            AttestationMapper mapper
    ) {
        this.demandeRepo = demandeRepo;
        this.attestationRepo = attestationRepo;
        this.employeeRepo = employeeRepo;
        this.pdfGeneratorService = pdfGeneratorService;
        this.fileService= fileService;
        this.helper = helper;
        this.mapper = mapper;
    }


    public String generateNumeroAttestation() {
        int currentYear = LocalDate.now().getYear();

        int count = attestationRepo.countByYear(currentYear);

        int nextNumber = count + 1;

        return String.format("ATT-%d-%03d", currentYear, nextNumber);
    }

    /* ================== Attestation Requests ================== */

    public ApiListResponse<AttestationDTO> listRequests(
            Long employeeId,
            AttestationType type,
            AttestationDemandStatus status,
            int start,
            int length,
            String sortDir
    ) {
        int page = start / length;
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, length, Sort.by(direction, "createdAt"));

        // Build combined specification
        Specification<DemandeAttestation> spec = Specification
                .where(AttestationRequestsSpecifications.hasEmployeeId(employeeId))
                .and(AttestationRequestsSpecifications.hasType(type))
                .and(AttestationRequestsSpecifications.hasStatus(status));

        Page<DemandeAttestation> pageResult = demandeRepo.findAll(spec, pageable);

        List<AttestationDTO> data = mapper.toAttestationDTOList(pageResult.getContent());

        return new ApiListResponse<>(
                "success",
                "Liste des demandes d'attestation récupérée avec succès",
                data,
                pageResult.getTotalElements(),
                pageResult.getTotalElements()
        );
    }


    public AttestationDTO createRequest(Long employeeId, AttestationType type, LocalDate dateSouhaitee, String note) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new BusinessException("Employee not found: " + employeeId));

        DemandeAttestation request = new DemandeAttestation();
        request.setEmployee(employee);
        request.setTypeAttestation(type);
        request.setDateRequest(LocalDate.now());
        request.setDateSouhaitee(dateSouhaitee);
        request.setStatus(AttestationDemandStatus.PENDING);
        request.setNote(note);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        DemandeAttestation saved = demandeRepo.save(request);
        return mapper.toAttestationDTO(saved);
    }

    public AttestationDTO getRequest(Long id) {
        DemandeAttestation request = demandeRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Attestation request not found: " + id));
        return mapper.toAttestationDTO(request);
    }

    public AttestationDTO updateRequestStatus(Long id, AttestationDemandStatus newStatus) {
        DemandeAttestation request = demandeRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Attestation request not found: " + id));

        if (request.getStatus() != AttestationDemandStatus.PENDING) {
            throw new BusinessException("Cannot update status. Current status: " + request.getStatus());
        }

        if (newStatus == AttestationDemandStatus.APPROVED) {
            request.setStatus(AttestationDemandStatus.APPROVED);
            request.setDateValidation(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            demandeRepo.save(request);

            Attestation attestation = new Attestation();
            attestation.setDemandeAttestation(request);
            attestation.setTypeAttestation(request.getTypeAttestation());
            attestation.setNumeroAttestation("AT-" + LocalDate.now().getYear() + "-" + request.getId());
            attestation.setDateGeneration(LocalDateTime.now());
            attestation.setCreatedAt(LocalDateTime.now());
            attestation.setUpdatedAt(LocalDateTime.now());
            attestationRepo.save(attestation);
        } else if (newStatus == AttestationDemandStatus.REJECTED) {
            request.setStatus(AttestationDemandStatus.REJECTED);
            request.setDateValidation(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            demandeRepo.save(request);
        } else {
            throw new BusinessException("Invalid status transition: " + newStatus);
        }

        return mapper.toAttestationDTO(request);
    }

    public void deleteRequest(Long id) {
        DemandeAttestation request = demandeRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Attestation request not found: " + id));

        demandeRepo.delete(request);
    }

    /* ================== Attestations ================== */

    public ApiListResponse<AttestationRequestDTO> listAttestations(
            Long employeeId,
            AttestationType type,
            String numero,
            int start,
            int length,
            String sortDir
    ) {
        int page = start / length;
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, length, Sort.by(direction, "createdAt"));

        // Build combined Specification
        Specification<Attestation> spec = Specification
                .where(AttestationSpecifications.hasEmployeeId(employeeId))
                .and(AttestationSpecifications.hasType(type))
                .and(AttestationSpecifications.hasNumero(numero));

        Page<Attestation> pageResult = attestationRepo.findAll(spec, pageable);

        List<AttestationRequestDTO> data = mapper.toRequestDTOList(pageResult.getContent(),fileService);

        return new ApiListResponse<>(
                "success",
                "Liste des attestations récupérée avec succès",
                data,
                pageResult.getTotalElements(),
                pageResult.getTotalElements()
        );
    }


    public AttestationRequestDTO getAttestation(Long id) {
        Attestation attestation = attestationRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Attestation not found: " + id));
        return mapper.toRequestDTO(attestation,fileService);
    }

    @Transactional
    public void generateAttestation(
            Long requestId) {

        DemandeAttestation request = demandeRepo.findById(requestId)
                .orElseThrow(() -> new BusinessException("Request not found"));

        if (request.getStatus() != AttestationDemandStatus.APPROVED) {
            throw new BusinessException("Request must be APPROVED");
        }

        // 1️⃣ Create Attestation entity
        Attestation attestation = new Attestation();
        attestation.setDemandeAttestation(request);
        attestation.setTypeAttestation(request.getTypeAttestation());
        attestation.setNumeroAttestation(generateNumeroAttestation());
        attestation.setDateGeneration(LocalDateTime.now());
        attestation.setCreatedAt(LocalDateTime.now());
        attestation.setUpdatedAt(LocalDateTime.now());

        Attestation saved = attestationRepo.save(attestation);

        // 2️⃣ Prepare placeholders (minimal for now)
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("companyName", "Tarmiz");
        placeholders.put("companyAddress", "Catilla Business Tower Castilla Tanger");
        placeholders.put("city", "Tanger");
        placeholders.put("date", LocalDate.now().toString());

        // others intentionally left empty
        placeholders.put("employeeName", "");
        placeholders.put("employeePosition", "");

        // 3️⃣ Generate & store PDF
        pdfGeneratorService.generateAndStore(
                EntityType.ATTESTATION,
                saved.getId(),
                helper.mapPurpose(request.getTypeAttestation()),
                placeholders,
                "Attestation " + request.getTypeAttestation(),
                "Generated automatically"
        );
    }

    public AttestationRequestDTO updateAttestation(Long id, AttestationType type, String numero, LocalDateTime dateGeneration, String notes) {
        Attestation attestation = attestationRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Attestation not found: " + id));

        if (type != null) attestation.setTypeAttestation(type);
        if (numero != null) attestation.setNumeroAttestation(numero);
        if (dateGeneration != null) attestation.setDateGeneration(dateGeneration);
        if (notes != null) attestation.setNotes(notes);
        attestation.setUpdatedAt(LocalDateTime.now());

        Attestation saved = attestationRepo.save(attestation);
        return mapper.toRequestDTO(saved,fileService);
    }
}