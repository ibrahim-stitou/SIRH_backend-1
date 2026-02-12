package com.tarmiz.SIRH_backend.service.Amendment;

import com.tarmiz.SIRH_backend.enums.Contract.*;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.AmendmentMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantDetailDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantListDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.CreateAmendmentRequest;
import com.tarmiz.SIRH_backend.model.entity.Contract.*;
import com.tarmiz.SIRH_backend.specs.AmendmentSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.*;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.PosteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmendmentService {

    private final ContractRepository contractRepository;
    private final AmendmentRepository amendmentRepository;
    private final ContractSalaryRepository contractSalaryRepository;
    private final ContractScheduleRepository contractScheduleRepository;
    private final ContractJobRepository contractJobRepository;
    private final PosteRepository posteRepository;
    private final AmendmentMapper amendmentMapper;

    public ApiListResponse<AvenantListDTO> getAmendmentsList(
            int start,
            int length,
            String sortBy,
            String sortDir,
            AmendmentStatus status,
            AmendmentType typeModification,
            String contractReference,
            String objet
    ) {

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdDate";
        }

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        int page = start / length;

        Pageable pageable = PageRequest.of(page, length, Sort.by(direction, sortBy));

        Specification<Amendment> spec = Specification
                .where(AmendmentSpecifications.hasStatus(status))
                .and(AmendmentSpecifications.hasTypeModification(typeModification))
                .and(AmendmentSpecifications.hasContractReference(contractReference))
                .and(AmendmentSpecifications.hasObjetText(objet));

        Page<Amendment> pageResult = amendmentRepository.findAll(spec, pageable);

        List<AvenantListDTO> dtoList = pageResult.getContent()
                .stream()
                .map(amendmentMapper::toAvenantListDTO)
                .toList();

        return new ApiListResponse<>(
                "success",
                "Amendments retrieved successfully",
                dtoList,
                pageResult.getTotalElements(),
                pageResult.getTotalElements()
        );
    }

    @Transactional
    public AvenantDetailDTO getAmendmentDetail(Long amendmentId) {
        Amendment amendment = amendmentRepository.findById(amendmentId)
                .orElseThrow(() -> new EntityNotFoundException("Amendment not found"));
        return amendmentMapper.toAvenantDetailDTO(amendment);
    }

    @Transactional
    public Long createDraftAmendment(Long contractId, CreateAmendmentRequest dto) {

        // 1️⃣ Récupérer le contrat
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        // 🔹 Vérification existence avenant du même type avec effectiveDate future
        boolean existsFutureAmendment = contract.getAmendments().stream()
                .filter(a -> a.getTypeModification() == dto.getTypeModification())
                .anyMatch(a -> a.getEffectiveDate() != null && a.getEffectiveDate().isAfter(LocalDate.now())
                        && a.getStatus() != AmendmentStatus.REJETE);
        if (existsFutureAmendment) {
            throw new BusinessException(
                    "Un avenant du même type existe déjà pour ce contrat avec une date d'effet future. " +
                            "Vous ne pouvez pas créer un nouvel avenant avant la date d'effet de celui existant."
            );
        }

        // 2️⃣ Créer l'avenant DRAFT
        Amendment amendment = new Amendment();
        amendment.setContract(contract);
        amendment.setReference(dto.getReference());
        amendment.setNumero(calculateNextNumero(contract));
        amendment.setObjet(dto.getObjet());
        amendment.setMotif(dto.getMotif());
        amendment.setDescription(dto.getDescription());
        amendment.setTypeModification(dto.getTypeModification());
        amendment.setStatus(AmendmentStatus.BROUILLON);
        amendment.setAmendmentDate(LocalDate.now());
        amendment.setEffectiveDate(dto.getEffectiveDate());
        amendment.setNotes(dto.getNotes());

        amendmentRepository.save(amendment);

        // 3️⃣ Créer les lignes liées à l’avenant (inactives)
        switch (dto.getTypeModification()) {
            case SALARY -> createSalaryDraft(contract, amendment, dto.getChanges().getSalary());
            case SCHEDULE -> createScheduleDraft(contract, amendment, dto.getChanges().getSchedule());
            case JOB -> createJobDraft(contract, amendment, dto.getChanges().getJob());
        }

        return amendment.getId();
    }

    // ==================== SALARY ====================
    private void createSalaryDraft(Contract contract, Amendment amendment, CreateAmendmentRequest.SalaryChangeDTO dto) {
        ContractSalary activeSalary = contractSalaryRepository.findByContractIdAndActiveTrue(contract.getId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No active salary found"));

        ContractSalary newSalary = new ContractSalary();
        newSalary.setContract(contract);
        newSalary.setAmendment(amendment);
        newSalary.setActive(false);
        newSalary.setEffectiveDate(amendment.getEffectiveDate());

        // Champs modifiés ou copie de l'existant
        newSalary.setBaseSalary(dto.getBaseSalary() != null ? dto.getBaseSalary() : activeSalary.getBaseSalary());
        newSalary.setSalaryBrut(dto.getSalaryBrut() != null ? dto.getSalaryBrut() : activeSalary.getSalaryBrut());
        newSalary.setSalaryNet(dto.getSalaryNet() != null ? dto.getSalaryNet() : activeSalary.getSalaryNet());
        newSalary.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : activeSalary.getCurrency());
        newSalary.setPaymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : activeSalary.getPaymentMethod());
        newSalary.setPeriodicity(dto.getPeriodicity() != null ? dto.getPeriodicity() : activeSalary.getPeriodicity());
        newSalary.setPaymentDay(dto.getPaymentDay() != null ? dto.getPaymentDay() : activeSalary.getPaymentDay());

        contractSalaryRepository.save(newSalary);
    }

    // ==================== SCHEDULE ====================
    private void createScheduleDraft(Contract contract, Amendment amendment, CreateAmendmentRequest.ScheduleChangeDTO dto) {
        ContractSchedule activeSchedule = contractScheduleRepository.findByContractIdAndActiveTrue(contract.getId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No active schedule found"));

        ContractSchedule newSchedule = new ContractSchedule();
        newSchedule.setContract(contract);
        newSchedule.setAmendment(amendment);
        newSchedule.setActive(false);
        newSchedule.setEffectiveDate(amendment.getEffectiveDate());

        // Champs modifiés ou copie de l'existant
        newSchedule.setScheduleType(dto.getScheduleType() != null ? dto.getScheduleType() : activeSchedule.getScheduleType());
        newSchedule.setShiftWork(dto.getShiftWork() != null ? dto.getShiftWork() : activeSchedule.getShiftWork());
        newSchedule.setHoursPerDay(dto.getHoursPerDay() != null ? dto.getHoursPerDay() : activeSchedule.getHoursPerDay());
        newSchedule.setDaysPerWeek(dto.getDaysPerWeek() != null ? dto.getDaysPerWeek() : activeSchedule.getDaysPerWeek());
        newSchedule.setHoursPerWeek(dto.getHoursPerWeek() != null ? dto.getHoursPerWeek() : activeSchedule.getHoursPerWeek());
        newSchedule.setStartTime(dto.getStartTime() != null ? dto.getStartTime() : activeSchedule.getStartTime());
        newSchedule.setEndTime(dto.getEndTime() != null ? dto.getEndTime() : activeSchedule.getEndTime());
        newSchedule.setBreakDuration(dto.getBreakDuration() != null ? dto.getBreakDuration() : activeSchedule.getBreakDuration());
        newSchedule.setAnnualLeaveDays(dto.getAnnualLeaveDays() != null ? dto.getAnnualLeaveDays() : activeSchedule.getAnnualLeaveDays());
        newSchedule.setOtherLeaves(dto.getOtherLeaves() != null ? dto.getOtherLeaves() : activeSchedule.getOtherLeaves());

        contractScheduleRepository.save(newSchedule);
    }

    // ==================== JOB ====================
    private void createJobDraft(Contract contract, Amendment amendment, CreateAmendmentRequest.JobChangeDTO dto) {
        ContractJob activeJob = contractJobRepository.findByContractIdAndActiveTrue(contract.getId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No active job found"));

        ContractJob newJob = new ContractJob();
        newJob.setContract(contract);
        newJob.setAmendment(amendment);
        newJob.setActive(false);
        newJob.setEffectiveDate(amendment.getEffectiveDate());

        // Poste récupéré via repository
        Poste poste = dto.getPosteId() != null
                ? posteRepository.findById(dto.getPosteId())
                .orElseThrow(() -> new EntityNotFoundException("Poste not found"))
                : activeJob.getPoste();
        newJob.setPoste(poste);

        // Champs modifiés ou copie de l'existant
        newJob.setWorkMode(dto.getWorkMode() != null ? dto.getWorkMode() : activeJob.getWorkMode());
        newJob.setClassification(dto.getClassification() != null ? dto.getClassification() : activeJob.getClassification());
        newJob.setLevel(dto.getLevel() != null ? dto.getLevel() : activeJob.getLevel());
        newJob.setResponsibilities(dto.getResponsibilities() != null ? dto.getResponsibilities() : activeJob.getResponsibilities());

        contractJobRepository.save(newJob);
    }

    // ==================== UTIL ====================
    private Integer calculateNextNumero(Contract contract) {
        return contract.getAmendments().stream()
                .map(Amendment::getNumero)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }


    @Transactional
    public void applyAmendment(Long amendmentId) {

        Amendment amendment = amendmentRepository.findById(amendmentId)
                .orElseThrow(() -> new EntityNotFoundException("Amendment not found"));

        if (amendment.getStatus() != AmendmentStatus.BROUILLON) {
            throw new IllegalStateException("Only DRAFT amendments can be applied");
        }

        Contract contract = amendment.getContract();

        if (contract == null) {
            throw new IllegalStateException("Amendment is not linked to a contract");
        }

        switch (amendment.getTypeModification()) {

            /* ===================== SALARY ===================== */
            case SALARY -> {

                List<ContractSalary> amendmentSalaries =
                        contractSalaryRepository.findByAmendmentId(amendmentId);

                if (amendmentSalaries.isEmpty()) {
                    throw new IllegalStateException("No salary lines found for this amendment");
                }

                // Désactiver les salaries actifs existants
                contractSalaryRepository
                        .findByContractIdAndActiveTrue(contract.getId())
                        .forEach(s -> s.setActive(false));

                contractSalaryRepository.flush();

                // Activer toutes les nouvelles lignes
                amendmentSalaries.forEach(s -> s.setActive(true));
            }

            /* ===================== SCHEDULE ===================== */
            case SCHEDULE -> {

                List<ContractSchedule> amendmentSchedules =
                        contractScheduleRepository.findByAmendmentId(amendmentId);

                if (amendmentSchedules.isEmpty()) {
                    throw new IllegalStateException("No schedule lines found for this amendment");
                }

                List<ContractSchedule> oldActiveSchedules =
                        contractScheduleRepository.findByContractIdAndActiveTrue(contract.getId());

                for (ContractSchedule oldSchedule : oldActiveSchedules) {
                    oldSchedule.setActive(false);
                }
                contractScheduleRepository.saveAll(oldActiveSchedules);

                contractScheduleRepository.flush();

                for (ContractSchedule newSchedule : amendmentSchedules) {
                    newSchedule.setActive(true);
                }
                contractScheduleRepository.saveAll(amendmentSchedules);
            }

            /* ===================== JOB ===================== */
            case JOB -> {
                List<ContractJob> amendmentJobs =
                        contractJobRepository.findByAmendmentId(amendmentId);

                if (amendmentJobs.isEmpty()) {
                    throw new IllegalStateException("No job lines found for this amendment");
                }

                List<ContractJob> oldActiveJobs =
                        contractJobRepository.findByContractIdAndActiveTrue(contract.getId());
                for (ContractJob oldJob : oldActiveJobs) {
                    oldJob.setActive(false);
                }
                contractJobRepository.saveAll(oldActiveJobs);

                contractJobRepository.flush();

                for (ContractJob newJob : amendmentJobs) {
                    newJob.setActive(true);
                }
                contractJobRepository.saveAll(amendmentJobs);
            }
        }

        amendment.setStatus(AmendmentStatus.VALIDE);
    }

    @Transactional
    public Amendment updateDraftAmendment(Long amendmentId, CreateAmendmentRequest dto) {
        Amendment amendment = amendmentRepository.findById(amendmentId)
                .orElseThrow(() -> new EntityNotFoundException("Amendment not found"));

        if (amendment.getStatus() != AmendmentStatus.BROUILLON) {
            throw new BusinessException(
                    "Cet avenant ne peut pas être modifié car il a déjà été validé."
            );
        }

        // ===================== UPDATE FIELDS =====================
        amendment.setReference(dto.getReference());
        amendment.setObjet(dto.getObjet());
        amendment.setMotif(dto.getMotif());
        amendment.setDescription(dto.getDescription());
        amendment.setNotes(dto.getNotes());
        amendment.setTypeModification(dto.getTypeModification());
        amendment.setEffectiveDate(dto.getEffectiveDate());

        // ===================== UPDATE RELATED DRAFT LINES =====================
        switch (dto.getTypeModification()) {
            case SALARY -> {
                CreateAmendmentRequest.SalaryChangeDTO salaryDTO = dto.getChanges().getSalary();
                if (salaryDTO != null) {
                    ContractSalary draftSalary = amendment.getSalaries().stream().findFirst()
                            .orElseThrow(() -> new IllegalStateException("No draft salary line found"));
                    draftSalary.setBaseSalary(salaryDTO.getBaseSalary());
                    draftSalary.setSalaryBrut(salaryDTO.getSalaryBrut());
                    draftSalary.setSalaryNet(salaryDTO.getSalaryNet());
                    draftSalary.setCurrency(salaryDTO.getCurrency() != null ? salaryDTO.getCurrency() : draftSalary.getCurrency());
                    draftSalary.setPaymentMethod(salaryDTO.getPaymentMethod());
                    draftSalary.setPeriodicity(salaryDTO.getPeriodicity());
                    draftSalary.setPaymentDay(salaryDTO.getPaymentDay());
                }
            }

            case SCHEDULE -> {
                CreateAmendmentRequest.ScheduleChangeDTO scheduleDTO = dto.getChanges().getSchedule();
                if (scheduleDTO != null) {
                    ContractSchedule draftSchedule = amendment.getSchedules().stream().findFirst()
                            .orElseThrow(() -> new IllegalStateException("No draft schedule line found"));
                    draftSchedule.setScheduleType(scheduleDTO.getScheduleType() != null ? scheduleDTO.getScheduleType() : draftSchedule.getScheduleType());
                    draftSchedule.setShiftWork(scheduleDTO.getShiftWork() != null ? scheduleDTO.getShiftWork() : draftSchedule.getShiftWork());
                    draftSchedule.setHoursPerDay(scheduleDTO.getHoursPerDay() != null ? scheduleDTO.getHoursPerDay() : draftSchedule.getHoursPerDay());
                    draftSchedule.setDaysPerWeek(scheduleDTO.getDaysPerWeek() != null ? scheduleDTO.getDaysPerWeek() : draftSchedule.getDaysPerWeek());
                    draftSchedule.setHoursPerWeek(scheduleDTO.getHoursPerWeek() != null ? scheduleDTO.getHoursPerWeek() : draftSchedule.getHoursPerWeek());
                    draftSchedule.setStartTime(scheduleDTO.getStartTime() != null ? scheduleDTO.getStartTime() : draftSchedule.getStartTime());
                    draftSchedule.setEndTime(scheduleDTO.getEndTime() != null ? scheduleDTO.getEndTime() : draftSchedule.getEndTime());
                    draftSchedule.setBreakDuration(scheduleDTO.getBreakDuration() != null ? scheduleDTO.getBreakDuration() : draftSchedule.getBreakDuration());
                    draftSchedule.setAnnualLeaveDays(scheduleDTO.getAnnualLeaveDays() != null ? scheduleDTO.getAnnualLeaveDays() : draftSchedule.getAnnualLeaveDays());
                    draftSchedule.setOtherLeaves(scheduleDTO.getOtherLeaves() != null ? scheduleDTO.getOtherLeaves() : draftSchedule.getOtherLeaves());
                }
            }

            case JOB -> {
                CreateAmendmentRequest.JobChangeDTO jobDTO = dto.getChanges().getJob();
                if (jobDTO != null) {
                    ContractJob draftJob = amendment.getJobs().stream().findFirst()
                            .orElseThrow(() -> new IllegalStateException("No draft job line found"));
                    draftJob.setWorkMode(jobDTO.getWorkMode() != null ? jobDTO.getWorkMode() : draftJob.getWorkMode());
                    draftJob.setClassification(jobDTO.getClassification() != null ? jobDTO.getClassification() : draftJob.getClassification());
                    draftJob.setLevel(jobDTO.getLevel() != null ? jobDTO.getLevel() : draftJob.getLevel());
                    draftJob.setResponsibilities(jobDTO.getResponsibilities() != null ? jobDTO.getResponsibilities() : draftJob.getResponsibilities());
                }
            }
        }

        return amendmentRepository.save(amendment);
    }


    /* ================= GET AVENANTS BY CONTRACT ================= */
    public List<AvenantDetailDTO> getAmendmentsByContractId(Long contractId) {
        List<Amendment> amendments = amendmentRepository
                .findByContractIdOrderByAmendmentDateDesc(contractId);

        if (amendments.isEmpty()) {
            throw new BusinessException("Aucun amendement trouvé pour le contrat avec id=" + contractId);
        }

        return amendments.stream()
                .map(amendmentMapper::toAvenantDetailDTO)
                .toList();
    }

    /* ================= DELETE DRAFT ================= */
    public void deleteDraftAmendment(Long amendmentId) {
        Amendment amendment = amendmentRepository.findById(amendmentId)
                .orElseThrow(() -> new EntityNotFoundException("Amendment not found"));

        if (amendment.getStatus() != AmendmentStatus.BROUILLON) {
            throw new IllegalStateException("Only DRAFT amendments can be deleted");
        }
        amendmentRepository.delete(amendment);
    }
}