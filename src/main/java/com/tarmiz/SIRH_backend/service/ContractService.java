package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.Contract.*;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.ContractDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.ContractMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractCreationDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractDetailsDTO;
import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.*;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import com.tarmiz.SIRH_backend.model.repository.*;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ClauseRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ContractClauseRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ContractRepository;
import com.tarmiz.SIRH_backend.model.repository.FilesRepos.FileRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.PosteRepository;
import com.tarmiz.SIRH_backend.service.document.FileService;
import com.tarmiz.SIRH_backend.service.document.PdfGeneratorService;
import com.tarmiz.SIRH_backend.specs.ContractSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepo;
    private final PosteRepository posteRepo;
    private final ClauseRepository clauseRepo;
    private final ContractDetailsMapper contractDetailsMapper;
    private final ContractClauseRepository contractClauseRepo;
    private final PdfGeneratorService pdfGeneratorService;
    private final FileService fileService;
    private final FileRepository fileRepository;

    /**
     * Get a paginated list of ContractListDTO
     *
     * @param start zero-based start index
     * @param length number of records to fetch
     * @return list of ContractListDTO
     */
    public ApiListResponse<ContractListDTO> getAllContracts(
            int start,
            int length,
            String sortDir,
            Long employeeId,
            ContractTypeEnum type,
            ContractStatusEnum status,
            Long departmentId
    ) {
        int page = start / length;

        Sort.Direction direction =
                "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, length, Sort.by(direction, "createdDate"));

        Specification<Contract> spec = Specification
                .where(ContractSpecifications.hasEmployeeId(employeeId))
                .and(ContractSpecifications.hasType(type))
                .and(ContractSpecifications.hasStatus(status))
                .and(ContractSpecifications.hasDepartmentId(departmentId));

        Page<Contract> contractPage = contractRepository.findAll(spec, pageable);

        List<ContractListDTO> data = contractPage.getContent()
                .stream()
                .map(ContractMapper::toContractListDTO)
                .toList();

        return new ApiListResponse<>(
                "success",
                "Contracts retrieved successfully",
                data,
                contractPage.getTotalElements(),
                contractPage.getTotalElements()
        );
    }

    /**
     * Get full contract details by ID, including trial period, job, schedule, salary, signed documents, audit.
     *
     * @param contractId ID of the contract
     * @return ContractDetailsDTO
     */
    public ContractDetailsDTO getContractDetails(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException("Contract not found with id=" + contractId));

        List<File> files = fileRepository.findByEntityTypeAndEntityIdAndDeletedAtIsNull(
                EntityType.CONTRACT, contractId
        );

        return contractDetailsMapper.toDTO(contract, files);
    }

    @Transactional
    public ContractDetailsDTO createContract(ContractCreationDTO dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (Exception e) {
            log.warn("Failed to serialize DTO for logging", e);
        }

        // --- 1. Find Employee ---
        if (dto.getEmployeeId() == null) {
            throw new BusinessException("L'ID de l'employé ne peut pas être null");
        }

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new BusinessException("Employé non trouvé avec id=" + dto.getEmployeeId()));

        // --- 2. Find Poste ---
        if (dto.getJob() == null) {
            throw new BusinessException("Les informations du poste sont requises");
        }
        if (dto.getJob().getPosteId() == null) {
            throw new BusinessException("L'ID du poste ne peut pas être null");
        }

        Long posteId = dto.getJob().getPosteId();
        Poste poste = posteRepo.findById(posteId)
                .orElseThrow(() -> new BusinessException("Poste non trouvé avec id=" + posteId));

        // --- 3. Create Contract ---
        Contract contract = new Contract();
        contract.setReference(dto.getReference());
        contract.setEmployee(employee);
        contract.setType(dto.getType());
        contract.setStatus(ContractStatusEnum.BROUILLON);
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setSignatureDate(dto.getSignatureDate());
        contract.setDescription(dto.getDescription());

        // Initialize collections
        contract.setJobs(new ArrayList<>());
        contract.setSalaries(new ArrayList<>());
        contract.setSchedules(new ArrayList<>());
        contract.setContractClauses(new ArrayList<>());

        Contract savedContract = contractRepository.save(contract);

        // --- 4. ContractJob ---
        if (dto.getJob() != null) {
            ContractJob job = new ContractJob();
            job.setContract(savedContract);
            job.setPoste(poste);
            job.setWorkMode(dto.getJob().getWorkMode() != null
                    ? WorkModeEnum.valueOf(dto.getJob().getWorkMode())
                    : null);
            job.setClassification(dto.getJob().getClassification());
            job.setLevel(dto.getJob().getLevel() != null
                    ? WorkLevelEnum.valueOf(dto.getJob().getLevel())
                    : null);
            job.setResponsibilities(dto.getJob().getResponsibilities());


            // Set initial state (no amendment)
            job.setAmendment(null);
            job.setEffectiveDate(dto.getStartDate());
            job.setActive(true);

            savedContract.getJobs().add(job);
        } else {
            log.warn("Job DTO is null, skipping ContractJob creation");
        }

        // --- 5. ContractClauses ---
        if (dto.getConditions() != null && dto.getConditions().getSelectedConditionIds() != null) {
            final Contract finalContract = savedContract;

            List<ContractClause> contractClauses = dto.getConditions().getSelectedConditionIds().stream()
                    .filter(clauseId -> {
                        if (clauseId == null) {
                            log.warn("Found null clause ID in list, skipping");
                            return false;
                        }
                        return true;
                    })
                    .map(clauseId -> {
                        Clause clause = clauseRepo.findById(clauseId)
                                .orElseThrow(() -> {
                                    log.error("Clause not found with id={}", clauseId);
                                    return new BusinessException("Clause non trouvée avec id=" + clauseId);
                                });

                        ContractClause cc = new ContractClause();
                        cc.setContract(finalContract);
                        cc.setClause(clause);
                        cc.setId(new ContractClauseId(finalContract.getId(), clause.getId()));
                        return cc;
                    })
                    .collect(Collectors.toList());

            savedContract.setContractClauses(contractClauses);
        } else {
            log.debug("No conditions or selectedConditionIds provided, skipping clauses");
        }

        // --- 6. TrialPeriod ---
        if (dto.getTrialPeriod() != null && Boolean.TRUE.equals(dto.getTrialPeriod().getEnabled())) {
            ContractCreationDTO.TrialPeriodDTO t = dto.getTrialPeriod();
            TrialPeriod trial = new TrialPeriod();
            trial.setContract(savedContract);
            trial.setEnabled(t.getEnabled());
            trial.setDurationMonths(t.getDurationMonths() != null ? t.getDurationMonths() : 0);
            trial.setDurationDays(t.getDurationDays() != null ? t.getDurationDays() : 0);

            if (t.getStartDate() != null) {
                trial.setStartDate(t.getStartDate());
            } else if (dto.getStartDate() != null) {
                trial.setStartDate(dto.getStartDate());
            } else {
                log.warn("TrialPeriod startDate is null, setting it to today by default");
            }

            trial.setEndDate(t.getEndDate());
            trial.setRenewable(t.getRenewable() != null ? t.getRenewable() : false);
            trial.setMaxRenewals(t.getMaxRenewals() != null ? t.getMaxRenewals() : 0);
            savedContract.setTrialPeriod(trial);
        } else {
            log.debug("Trial period not enabled or null, skipping");
        }

        // --- 7. ContractSalary ---
        if (dto.getSalary() != null) {
            ContractCreationDTO.ContractSalaryDTO s = dto.getSalary();
            ContractSalary salary = new ContractSalary();
            salary.setContract(savedContract);

            if (s.getSalaryBase() != null) {
                salary.setBaseSalary(s.getSalaryBase());
            } else {
                log.warn("Base salary is null, setting to 0 by default");
                salary.setBaseSalary(BigDecimal.ZERO);
            }

            salary.setSalaryBrut(s.getSalaryBrut());
            salary.setSalaryNet(s.getSalaryNet());
            salary.setCurrency(s.getCurrency() != null ? s.getCurrency() : "MAD");
            salary.setPaymentMethod(s.getPaymentMethod() != null
                    ? PaymentMethodEnum.valueOf(s.getPaymentMethod())
                    : null);
            salary.setPeriodicity(s.getPeriodicity() != null
                    ? PaymentPeriodicityEnum.valueOf(s.getPeriodicity())
                    : null);
            salary.setPaymentDay(s.getPaymentDay());

            // Set initial state (no amendment)
            salary.setAmendment(null);
            salary.setEffectiveDate(dto.getStartDate());
            salary.setActive(true);

            // --- 7.a Handle Primes ---
            if (s.getPrimes() != null && !s.getPrimes().isEmpty()) {
                List<Prime> primes = s.getPrimes().stream()
                        .filter(pdto -> pdto != null && pdto.getPrimeTypeId() != null)
                        .map(pdto -> {
                            PrimeTypeIdEnum primeType = pdto.getPrimeTypeId();

                            Prime prime = new Prime();
                            prime.setContractSalary(salary);
                            prime.setPrimeTypeId(primeType);
                            prime.setLabel(primeType.getLabel());
                            prime.setAmount(pdto.getAmount() != null ? pdto.getAmount() : BigDecimal.ZERO);
                            prime.setIsTaxable(pdto.getIsTaxable() != null ? pdto.getIsTaxable() : true);
                            prime.setIsSubjectToCnss(pdto.getIsSubjectToCnss() != null ? pdto.getIsSubjectToCnss() : true);

                            return prime;
                        })
                        .collect(Collectors.toList());
                salary.setPrimes(primes);
            }

            savedContract.getSalaries().add(salary);
        } else {
            log.debug("No salary information provided, skipping");
        }

        // --- 8. ContractSchedule ---
        if (dto.getSchedule() != null) {
            ContractCreationDTO.ContractScheduleDTO sc = dto.getSchedule();
            ContractSchedule schedule = new ContractSchedule();
            schedule.setContract(savedContract);
            schedule.setShiftWork(sc.getWorkShift() != null ? sc.getWorkShift() : false);

            if (sc.getScheduleType() != null) {
                schedule.setScheduleType(ScheduleTypeEnum.valueOf(sc.getScheduleType()));
            }

            schedule.setHoursPerDay(sc.getHoursPerDay());
            schedule.setDaysPerWeek(sc.getDaysPerWeek());
            schedule.setHoursPerWeek(sc.getHoursPerWeek());
            schedule.setStartTime(sc.getStartTime());
            schedule.setEndTime(sc.getEndTime());
            schedule.setBreakDuration(sc.getBreakDuration());
            schedule.setAnnualLeaveDays(sc.getAnnualLeaveDays());
            schedule.setOtherLeaves(sc.getOtherLeaves());

            // Set initial state (no amendment)
            schedule.setAmendment(null);
            schedule.setEffectiveDate(dto.getStartDate());
            schedule.setActive(true);

            savedContract.getSchedules().add(schedule);
        } else {
            log.debug("No schedule information provided, skipping");
        }

        Contract finalSavedContract = contractRepository.save(savedContract);
        log.info("========== Contract creation completed successfully. Contract ID: {} ==========",
                finalSavedContract.getId());

        // --- 9. Map to DTO ---
        ContractDetailsDTO dtoResult = contractDetailsMapper.toDTO(finalSavedContract, null);

        return dtoResult;
    }

    @Transactional
    public void updateContract(Long contractId, ContractCreationDTO dto) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException("Contrat introuvable id=" + contractId));

        if (contract.getStatus() != ContractStatusEnum.BROUILLON) {
            throw new BusinessException(
                    "Vous n'avez pas le droit de modifier le contrat, il est déjà actif ou dans un état non modifiable."
            );
        }

        /* ===== Champs simples ===== */
        if (dto.getStartDate() != null) contract.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) contract.setEndDate(dto.getEndDate());
        if (dto.getSignatureDate() != null) contract.setSignatureDate(dto.getSignatureDate());
        if (dto.getType() != null) contract.setType(ContractTypeEnum.valueOf(dto.getType().name()));
        if (dto.getDescription() != null) contract.setDescription(dto.getDescription());

        /* ===== Trial Period ===== */
        if (dto.getTrialPeriod() != null && contract.getTrialPeriod() != null) {
            TrialPeriod tp = contract.getTrialPeriod();
            ContractCreationDTO.TrialPeriodDTO tpd = dto.getTrialPeriod();

            if (tpd.getEnabled() != null) tp.setEnabled(tpd.getEnabled());
            if (tpd.getDurationMonths() != null) tp.setDurationMonths(tpd.getDurationMonths());
            if (tpd.getDurationDays() != null) tp.setDurationDays(tpd.getDurationDays());
            if (tpd.getEndDate() != null) tp.setEndDate(tpd.getEndDate());
            if (tpd.getRenewable() != null) tp.setRenewable(tpd.getRenewable());
            if (tpd.getMaxRenewals() != null) tp.setMaxRenewals(tpd.getMaxRenewals());
        }

        /* ===== Clauses (FULL REPLACE) ===== */
        if (dto.getConditions() != null && dto.getConditions().getSelectedConditionIds() != null) {
            contractClauseRepo.deleteByContractId(contract.getId());

            List<ContractClause> clauses = dto.getConditions().getSelectedConditionIds().stream()
                    .map(id -> {
                        Clause clause = clauseRepo.findById(Long.valueOf(id))
                                .orElseThrow(() -> new BusinessException("Clause inexistante id=" + id));

                        ContractClause cc = new ContractClause();
                        cc.setId(new ContractClauseId(contract.getId(), clause.getId()));
                        cc.setContract(contract);
                        cc.setClause(clause);
                        return cc;
                    }).collect(Collectors.toList());

            contract.setContractClauses(clauses);
        }

        /* ===== Job (update existing active job) ===== */
        if (dto.getJob() != null && dto.getJob().getPosteId() != null) {
            Poste poste = posteRepo.findById(Long.valueOf(dto.getJob().getPosteId()))
                    .orElseThrow(() -> new BusinessException("Poste introuvable"));

            // Get active job
            ContractJob activeJob = contract.getJobs().stream()
                    .filter(j -> j.getActive() != null && j.getActive())
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Aucun poste actif trouvé"));

            activeJob.setPoste(poste);

            if (dto.getJob().getWorkMode() != null) {
                activeJob.setWorkMode(WorkModeEnum.valueOf(dto.getJob().getWorkMode()));
            }
            if (dto.getJob().getLevel() != null) {
                activeJob.setLevel(WorkLevelEnum.valueOf(dto.getJob().getLevel()));
            }
            if (dto.getJob().getClassification() != null) {
                activeJob.setClassification(dto.getJob().getClassification());
            }
            if (dto.getJob().getResponsibilities() != null) {
                activeJob.setResponsibilities(dto.getJob().getResponsibilities());
            }
        }

        /* ===== Salary (update existing active salary) ===== */
        if (dto.getSalary() != null) {
            ContractSalary activeSalary = contract.getSalaries().stream()
                    .filter(s -> s.getActive() != null && s.getActive())
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Aucun salaire actif trouvé"));

            if (dto.getSalary().getSalaryBase() != null) {
                activeSalary.setBaseSalary(dto.getSalary().getSalaryBase());
            }
            if (dto.getSalary().getSalaryBrut() != null) {
                activeSalary.setSalaryBrut(dto.getSalary().getSalaryBrut());
            }
            if (dto.getSalary().getSalaryNet() != null) {
                activeSalary.setSalaryNet(dto.getSalary().getSalaryNet());
            }
            if (dto.getSalary().getPaymentMethod() != null) {
                activeSalary.setPaymentMethod(PaymentMethodEnum.valueOf(dto.getSalary().getPaymentMethod()));
            }
            if (dto.getSalary().getPeriodicity() != null) {
                activeSalary.setPeriodicity(PaymentPeriodicityEnum.valueOf(dto.getSalary().getPeriodicity()));
            }
            if (dto.getSalary().getPaymentDay() != null) {
                activeSalary.setPaymentDay(dto.getSalary().getPaymentDay());
            }

            // Handle primes update
            if (dto.getSalary().getPrimes() != null) {
                // Clear existing primes
                if (activeSalary.getPrimes() != null) {
                    activeSalary.getPrimes().clear();
                } else {
                    activeSalary.setPrimes(new ArrayList<>());
                }

                // Add new primes
                List<Prime> primes = dto.getSalary().getPrimes().stream()
                        .filter(pdto -> pdto != null && pdto.getPrimeTypeId() != null)
                        .map(pdto -> {
                            Prime prime = new Prime();
                            prime.setContractSalary(activeSalary);
                            prime.setPrimeTypeId(pdto.getPrimeTypeId());
                            prime.setLabel(pdto.getPrimeTypeId().getLabel());
                            prime.setAmount(pdto.getAmount() != null ? pdto.getAmount() : BigDecimal.ZERO);
                            prime.setIsTaxable(pdto.getIsTaxable() != null ? pdto.getIsTaxable() : true);
                            prime.setIsSubjectToCnss(pdto.getIsSubjectToCnss() != null ? pdto.getIsSubjectToCnss() : true);
                            return prime;
                        })
                        .collect(Collectors.toList());

                activeSalary.getPrimes().addAll(primes);
            }
        }

        /* ===== Schedule (update existing active schedule) ===== */
        if (dto.getSchedule() != null) {
            ContractSchedule activeSchedule = contract.getSchedules().stream()
                    .filter(sc -> sc.getActive() != null && sc.getActive())
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Aucun horaire actif trouvé"));

            if (dto.getSchedule().getScheduleType() != null) {
                activeSchedule.setScheduleType(ScheduleTypeEnum.valueOf(dto.getSchedule().getScheduleType()));
            }
            if (dto.getSchedule().getWorkShift() != null) {
                activeSchedule.setShiftWork(dto.getSchedule().getWorkShift());
            }
            if (dto.getSchedule().getHoursPerDay() != null) {
                activeSchedule.setHoursPerDay(dto.getSchedule().getHoursPerDay());
            }
            if (dto.getSchedule().getDaysPerWeek() != null) {
                activeSchedule.setDaysPerWeek(dto.getSchedule().getDaysPerWeek());
            }
            if (dto.getSchedule().getHoursPerWeek() != null) {
                activeSchedule.setHoursPerWeek(dto.getSchedule().getHoursPerWeek());
            }
            if (dto.getSchedule().getStartTime() != null) {
                activeSchedule.setStartTime(dto.getSchedule().getStartTime());
            }
            if (dto.getSchedule().getEndTime() != null) {
                activeSchedule.setEndTime(dto.getSchedule().getEndTime());
            }
            if (dto.getSchedule().getBreakDuration() != null) {
                activeSchedule.setBreakDuration(dto.getSchedule().getBreakDuration());
            }
            if (dto.getSchedule().getAnnualLeaveDays() != null) {
                activeSchedule.setAnnualLeaveDays(dto.getSchedule().getAnnualLeaveDays());
            }
            if (dto.getSchedule().getOtherLeaves() != null) {
                activeSchedule.setOtherLeaves(dto.getSchedule().getOtherLeaves());
            }
        }

        contractRepository.save(contract);
    }

    @Transactional
    public void validateContract(Long id) {
        Contract c = contractRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Contrat introuvable"));

        // Check if contract has at least one active job, salary, and schedule
        boolean hasActiveJob = c.getJobs() != null && c.getJobs().stream()
                .anyMatch(j -> j.getActive() != null && j.getActive());
        boolean hasActiveSalary = c.getSalaries() != null && c.getSalaries().stream()
                .anyMatch(s -> s.getActive() != null && s.getActive());
        boolean hasActiveSchedule = c.getSchedules() != null && c.getSchedules().stream()
                .anyMatch(sc -> sc.getActive() != null && sc.getActive());

        if (c.getEmployee() == null || !hasActiveJob || !hasActiveSalary || !hasActiveSchedule) {
            throw new BusinessException("Le contrat est incomplet et ne peut pas être validé");
        }

        c.setStatus(ContractStatusEnum.ACTIF);
        contractRepository.save(c);
    }

    @Transactional
    public void cancelContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException("Contrat introuvable avec id=" + contractId));

        contract.setStatus(ContractStatusEnum.SUSPENDU);
        contractRepository.save(contract);
    }

    @Transactional
    public void delete(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Contrat non trouvé avec id=" + id));

        if (contract.getStatus() != ContractStatusEnum.BROUILLON) {
            throw new BusinessException("Impossible de supprimer un contrat déjà actif ou validé");
        }

        if (contract.getContractClauses() != null) {
            contract.getContractClauses().forEach(cc -> cc.setContract(null));
            contract.getContractClauses().clear();
        }

        contract.setEmployee(null);
        contractRepository.delete(contract);
    }

    @Transactional
    public void generateContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException("Contract not found"));

        if (!EnumSet.of(
                ContractStatusEnum.BROUILLON,
                ContractStatusEnum.EN_ATTENTE_SIGNATURE,
                ContractStatusEnum.ACTIF
        ).contains(contract.getStatus())) {
            throw new BusinessException("Contract status not allowed for generation");
        }

        // Get active records
        ContractJob activeJob = contract.getJobs() != null ? contract.getJobs().stream()
                .filter(j -> j.getActive() != null && j.getActive())
                .findFirst()
                .orElse(null) : null;

        ContractSalary activeSalary = contract.getSalaries() != null ? contract.getSalaries().stream()
                .filter(s -> s.getActive() != null && s.getActive())
                .findFirst()
                .orElse(null) : null;

        ContractSchedule activeSchedule = contract.getSchedules() != null ? contract.getSchedules().stream()
                .filter(sc -> sc.getActive() != null && sc.getActive())
                .findFirst()
                .orElse(null) : null;

        // 1️⃣ Préparer les placeholders
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("company_name", "Tarmiz");
        placeholders.put("company_address", "Catilla Business Tower Castilla");
        placeholders.put("company_city", "Tanger");
        placeholders.put("company_country", "Maroc");
        placeholders.put("company_phone", "XXX-XXX-XXX");
        placeholders.put("company_email", "contact@tarmiz.com");

        placeholders.put("contract_reference", contract.getReference());
        placeholders.put("contract_type", contract.getType().name());
        placeholders.put("contract_status", contract.getStatus().name());
        placeholders.put("signature_date", contract.getSignatureDate() != null ? contract.getSignatureDate().toString() : "");
        placeholders.put("start_date", contract.getStartDate() != null ? contract.getStartDate().toString() : "");
        placeholders.put("end_date", contract.getEndDate() != null ? contract.getEndDate().toString() : "");
        placeholders.put("contract_description", contract.getDescription() != null ? contract.getDescription() : "");

        placeholders.put("employee_name", contract.getEmployee().getFirstName() + " " + contract.getEmployee().getLastName());
        placeholders.put("employee_matricule", contract.getEmployee().getMatricule());
        placeholders.put("employee_id", contract.getEmployee().getId().toString());

        // Job (from active job)
        if (activeJob != null) {
            placeholders.put("job_metier", activeJob.getPoste().getEmploi().getMetier().getLibelle());
            placeholders.put("job_emploi", activeJob.getPoste().getEmploi().getLibelle());
            placeholders.put("job_poste", activeJob.getPoste().getLibelle());
            placeholders.put("job_level", activeJob.getLevel() != null ? activeJob.getLevel().name() : "N/A");
            placeholders.put("job_work_mode", activeJob.getWorkMode() != null ? activeJob.getWorkMode().name() : "N/A");
            placeholders.put("job_classification", activeJob.getClassification() != null ? activeJob.getClassification() : "N/A");
            placeholders.put("job_responsibilities", activeJob.getResponsibilities() != null ? activeJob.getResponsibilities() : "N/A");
        } else {
            placeholders.put("job_metier", "N/A");
            placeholders.put("job_emploi", "N/A");
            placeholders.put("job_poste", "N/A");
            placeholders.put("job_level", "N/A");
            placeholders.put("job_work_mode", "N/A");
            placeholders.put("job_classification", "N/A");
            placeholders.put("job_responsibilities", "N/A");
        }

        // Trial Period
        if (contract.getTrialPeriod() != null && contract.getTrialPeriod().getEnabled()) {
            placeholders.put("trial_enabled", "Oui");
            placeholders.put("trial_duration_months", String.valueOf(contract.getTrialPeriod().getDurationMonths()));
            placeholders.put("trial_duration_days", String.valueOf(contract.getTrialPeriod().getDurationDays()));
            placeholders.put("trial_end_date", contract.getTrialPeriod().getEndDate() != null ? contract.getTrialPeriod().getEndDate().toString() : "");
            placeholders.put("trial_renewable", contract.getTrialPeriod().getRenewable() ? "Oui" : "Non");
            placeholders.put("trial_max_renewals", String.valueOf(contract.getTrialPeriod().getMaxRenewals()));
            placeholders.put("trial_conditions", contract.getTrialPeriod().getConditions() != null ? contract.getTrialPeriod().getConditions() : "");

            if (contract.getTrialPeriod().getTrialPeriodCriteriaList() != null && !contract.getTrialPeriod().getTrialPeriodCriteriaList().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                contract.getTrialPeriod().getTrialPeriodCriteriaList().forEach(tc ->
                        sb.append(String.format("%d - %s - %s\n", tc.getTrialCriteria().getId(),
                                tc.getTrialCriteria().getName(), tc.getTrialCriteria().getDescription()))
                );
                placeholders.put("trial_criteria", sb.toString());
            } else {
                placeholders.put("trial_criteria", "Aucun critère défini");
            }
        } else {
            placeholders.put("trial_enabled", "Non");
            placeholders.put("trial_duration_months", "0");
            placeholders.put("trial_duration_days", "0");
            placeholders.put("trial_end_date", "N/A");
            placeholders.put("trial_renewable", "Non");
            placeholders.put("trial_max_renewals", "0");
            placeholders.put("trial_conditions", "N/A");
            placeholders.put("trial_criteria", "N/A");
        }

        // Schedule (from active schedule)
        if (activeSchedule != null) {
            placeholders.put("schedule_type", activeSchedule.getScheduleType() != null ? activeSchedule.getScheduleType().name() : "N/A");
            placeholders.put("schedule_shift_work", activeSchedule.getShiftWork() != null && activeSchedule.getShiftWork() ? "Oui" : "Non");
            placeholders.put("schedule_hours_per_day", activeSchedule.getHoursPerDay() != null ? activeSchedule.getHoursPerDay().toString() : "N/A");
            placeholders.put("schedule_days_per_week", activeSchedule.getDaysPerWeek() != null ? activeSchedule.getDaysPerWeek().toString() : "N/A");
            placeholders.put("schedule_hours_per_week", activeSchedule.getHoursPerWeek() != null ? activeSchedule.getHoursPerWeek().toString() : "N/A");
            placeholders.put("schedule_start_time", activeSchedule.getStartTime() != null ? activeSchedule.getStartTime().toString() : "N/A");
            placeholders.put("schedule_end_time", activeSchedule.getEndTime() != null ? activeSchedule.getEndTime().toString() : "N/A");
            placeholders.put("schedule_break_duration", activeSchedule.getBreakDuration() != null ? activeSchedule.getBreakDuration().toString() : "N/A");
            placeholders.put("schedule_annual_leave_days", activeSchedule.getAnnualLeaveDays() != null ? activeSchedule.getAnnualLeaveDays().toString() : "N/A");
            placeholders.put("schedule_other_leaves", activeSchedule.getOtherLeaves() != null ? activeSchedule.getOtherLeaves() : "N/A");
        } else {
            placeholders.put("schedule_type", "N/A");
            placeholders.put("schedule_shift_work", "Non");
            placeholders.put("schedule_hours_per_day", "N/A");
            placeholders.put("schedule_days_per_week", "N/A");
            placeholders.put("schedule_hours_per_week", "N/A");
            placeholders.put("schedule_start_time", "N/A");
            placeholders.put("schedule_end_time", "N/A");
            placeholders.put("schedule_break_duration", "N/A");
            placeholders.put("schedule_annual_leave_days", "N/A");
            placeholders.put("schedule_other_leaves", "N/A");
        }

        // Salary (from active salary)
        if (activeSalary != null) {
            placeholders.put("salary_brut", activeSalary.getSalaryBrut() != null ? activeSalary.getSalaryBrut().toString() : "0");
            placeholders.put("salary_net", activeSalary.getSalaryNet() != null ? activeSalary.getSalaryNet().toString() : "0");
            placeholders.put("salary_currency", activeSalary.getCurrency() != null ? activeSalary.getCurrency() : "MAD");
            placeholders.put("salary_payment_method", activeSalary.getPaymentMethod() != null ? activeSalary.getPaymentMethod().name() : "N/A");
            placeholders.put("salary_periodicity", activeSalary.getPeriodicity() != null ? activeSalary.getPeriodicity().name() : "N/A");
        } else {
            placeholders.put("salary_brut", "0");
            placeholders.put("salary_net", "0");
            placeholders.put("salary_currency", "MAD");
            placeholders.put("salary_payment_method", "N/A");
            placeholders.put("salary_periodicity", "N/A");
        }

        // 2️⃣ Génération PDF
        pdfGeneratorService.generateAndStore(
                EntityType.CONTRACT,
                contract.getId(),
                FilePurpose.GENERATED,
                placeholders,
                "Contrat - " + contract.getReference(),
                "Generated automatically"
        );
    }

    @Transactional
    public File uploadSignedContract(Long contractId, MultipartFile multipartFile) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException("Contract not found"));

        return fileService.upload(
                multipartFile,
                EntityType.CONTRACT,
                contract.getId(),
                FilePurpose.SCANNED,
                "Signed Contract",
                "Uploaded signed contract PDF"
        );
    }

    /**
     * Helper method to get the active job for a contract
     */
    private ContractJob getActiveJob(Contract contract) {
        if (contract.getJobs() == null) {
            return null;
        }
        return contract.getJobs().stream()
                .filter(j -> j.getActive() != null && j.getActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper method to get the active salary for a contract
     */
    private ContractSalary getActiveSalary(Contract contract) {
        if (contract.getSalaries() == null) {
            return null;
        }
        return contract.getSalaries().stream()
                .filter(s -> s.getActive() != null && s.getActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper method to get the active schedule for a contract
     */
    private ContractSchedule getActiveSchedule(Contract contract) {
        if (contract.getSchedules() == null) {
            return null;
        }
        return contract.getSchedules().stream()
                .filter(sc -> sc.getActive() != null && sc.getActive())
                .findFirst()
                .orElse(null);
    }
}