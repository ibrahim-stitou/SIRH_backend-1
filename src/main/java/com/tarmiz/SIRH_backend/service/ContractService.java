package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.Contract.*;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.ContractDetailsMapper;
import com.tarmiz.SIRH_backend.mapper.ContractMapper;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

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
    private final PdfGeneratorService pdfGeneratorService ;
    private final FileService fileService;

    private final FileRepository fileRepository;

    /**
     * Get a paginated list of ContractListDTO
     *
     * @param start zero-based start index
     * @param length number of records to fetch
     * @return list of ContractListDTO
     */
    public List<ContractListDTO> getAllContracts(int start, int length) {
        int pageNumber = start / length;
        PageRequest pageable = PageRequest.of(pageNumber, length);

        Page<Contract> contractPage = contractRepository.findAll(pageable);

        return contractPage.stream()
                .map(ContractMapper::toContractListDTO)
                .collect(Collectors.toList());
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
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));

        // --- 1. Find Employee ---
        if (dto.getEmployeeId() == null) {
            throw new BusinessException("L'ID de l'employé ne peut pas être null");
        }

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> {
                    return new BusinessException("Employé non trouvé avec id=" + dto.getEmployeeId());
                });

        // --- 2. Find Poste ---
        if (dto.getJob() == null) {
            throw new BusinessException("Les informations du poste sont requises");
        }
        if (dto.getJob().getPosteId() == null) {
            throw new BusinessException("L'ID du poste ne peut pas être null");
        }

        Long posteId = dto.getJob().getPosteId();

        Poste poste = posteRepo.findById(posteId)
                .orElseThrow(() -> {
                    return new BusinessException("Poste non trouvé avec id=" + posteId);
                });

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
            job.setResponsibilities(dto.getJob().getResponsibilities());
            savedContract.setJob(job);
        } else {
            log.warn("Job DTO is null, skipping ContractJob creation");
        }

        // --- 5. ContractClauses ---
        if (dto.getConditions() != null && dto.getConditions().getSelectedConditionIds() != null) {
                    dto.getConditions().getSelectedConditionIds().size();

            final Contract finalContract = savedContract;

            List<ContractClause> contractClauses =
                    dto.getConditions().getSelectedConditionIds().stream()
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
                                            return new BusinessException(
                                                    "Clause non trouvée avec id=" + clauseId);
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
            }else {
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
                salary.setBaseSalary(s.getSalaryBase().intValue());
            } else {
                log.warn("Base salary is null, setting to 0 by default");
                salary.setBaseSalary(0);
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

                            prime.setAmount(
                                    pdto.getAmount() != null ? pdto.getAmount() : BigDecimal.ZERO
                            );

                            prime.setIsTaxable(
                                    pdto.getIsTaxable() != null ? pdto.getIsTaxable() : true
                            );

                            prime.setIsSubjectToCnss(
                                    pdto.getIsSubjectToCnss() != null ? pdto.getIsSubjectToCnss() : true
                            );

                            return prime;
                        })
                        .collect(Collectors.toList());
                salary.setPrimes(primes);
            }

            savedContract.setSalary(salary);

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
            savedContract.setSchedule(schedule);
        } else {
            log.debug("No schedule information provided, skipping");
        }

        Contract finalSavedContract = contractRepository.save(savedContract);
        log.info("========== Contract creation completed successfully. Contract ID: {} ==========",
                finalSavedContract.getId());

        // --- 10. Map to DTO ---
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

        /* ===== Job (association only) ===== */
        if (dto.getJob() != null && dto.getJob().getPosteId() != null) {
            Poste poste = posteRepo.findById(Long.valueOf(dto.getJob().getPosteId()))
                    .orElseThrow(() -> new BusinessException("Poste introuvable"));

            contract.getJob().setPoste(poste);
            if (dto.getJob().getWorkMode() != null)
                contract.getJob().setWorkMode(WorkModeEnum.valueOf(dto.getJob().getWorkMode()));
            if (dto.getJob().getClassification() != null)
                contract.getJob().setClassification(dto.getJob().getClassification());
            if (dto.getJob().getResponsibilities() != null)
                contract.getJob().setResponsibilities(dto.getJob().getResponsibilities());
        }

        /* ===== Salary ===== */
        if (dto.getSalary() != null && contract.getSalary() != null) {
            ContractSalary s = contract.getSalary();
            if (dto.getSalary().getSalaryBrut() != null) s.setSalaryBrut(dto.getSalary().getSalaryBrut());
            if (dto.getSalary().getSalaryNet() != null) s.setSalaryNet(dto.getSalary().getSalaryNet());
            if (dto.getSalary().getPaymentMethod() != null)
                s.setPaymentMethod(PaymentMethodEnum.valueOf(dto.getSalary().getPaymentMethod()));
        }

        /* ===== Schedule ===== */
        if (dto.getSchedule() != null && contract.getSchedule() != null) {
            ContractSchedule sc = contract.getSchedule();
            if (dto.getSchedule().getScheduleType() != null)
                sc.setScheduleType(ScheduleTypeEnum.valueOf(dto.getSchedule().getScheduleType()));
            if (dto.getSchedule().getWorkShift() != null)
                sc.setShiftWork(dto.getSchedule().getWorkShift());
        }

        contractRepository.save(contract);
    }

    @Transactional
    public void validateContract(Long id) {

        Contract c = contractRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Contrat introuvable"));

        if (c.getEmployee() == null || c.getJob() == null
                || c.getSalary() == null || c.getSchedule() == null) {
            throw new BusinessException("Le contrat est incomplet et ne peut pas être validé");
        }

        c.setStatus(ContractStatusEnum.ACTIF);
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

        // Job
        if (contract.getJob() != null) {
            placeholders.put("job_metier", contract.getJob().getPoste().getEmploi().getMetier().getLibelle());
            placeholders.put("job_emploi", contract.getJob().getPoste().getEmploi().getLibelle());
            placeholders.put("job_poste", contract.getJob().getPoste().getLibelle());
            placeholders.put("job_level", contract.getJob().getLevel() != null ? contract.getJob().getLevel().name() : "N/A");
            placeholders.put("job_work_mode", contract.getJob().getWorkMode() != null ? contract.getJob().getWorkMode().name() : "N/A");
            placeholders.put("job_classification", contract.getJob().getClassification() != null ? contract.getJob().getClassification() : "N/A");
            placeholders.put("job_responsibilities", contract.getJob().getResponsibilities() != null ? contract.getJob().getResponsibilities() : "N/A");
        } else {
            // Add default values for missing job info
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

            // critère d'acceptation
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

        // Schedule
        if (contract.getSchedule() != null) {
            placeholders.put("schedule_type", contract.getSchedule().getScheduleType() != null ? contract.getSchedule().getScheduleType().name() : "N/A");
            placeholders.put("schedule_shift_work", contract.getSchedule().getShiftWork() != null && contract.getSchedule().getShiftWork() ? "Oui" : "Non");
            placeholders.put("schedule_hours_per_day", contract.getSchedule().getHoursPerDay() != null ? contract.getSchedule().getHoursPerDay().toString() : "N/A");
            placeholders.put("schedule_days_per_week", contract.getSchedule().getDaysPerWeek() != null ? contract.getSchedule().getDaysPerWeek().toString() : "N/A");
            placeholders.put("schedule_hours_per_week", contract.getSchedule().getHoursPerWeek() != null ? contract.getSchedule().getHoursPerWeek().toString() : "N/A");
            placeholders.put("schedule_start_time", contract.getSchedule().getStartTime() != null ? contract.getSchedule().getStartTime().toString() : "N/A");
            placeholders.put("schedule_end_time", contract.getSchedule().getEndTime() != null ? contract.getSchedule().getEndTime().toString() : "N/A");
            placeholders.put("schedule_break_duration", contract.getSchedule().getBreakDuration() != null ? contract.getSchedule().getBreakDuration().toString() : "N/A");
            placeholders.put("schedule_annual_leave_days", contract.getSchedule().getAnnualLeaveDays() != null ? contract.getSchedule().getAnnualLeaveDays().toString() : "N/A");
            placeholders.put("schedule_other_leaves", contract.getSchedule().getOtherLeaves() != null ? contract.getSchedule().getOtherLeaves() : "N/A");
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

        // Salary
        if (contract.getSalary() != null) {
            placeholders.put("salary_brut", contract.getSalary().getSalaryBrut() != null ? contract.getSalary().getSalaryBrut().toString() : "0");
            placeholders.put("salary_net", contract.getSalary().getSalaryNet() != null ? contract.getSalary().getSalaryNet().toString() : "0");
            placeholders.put("salary_currency", contract.getSalary().getCurrency() != null ? contract.getSalary().getCurrency() : "MAD");
            placeholders.put("salary_payment_method", contract.getSalary().getPaymentMethod() != null ? contract.getSalary().getPaymentMethod().name() : "N/A");
            placeholders.put("salary_periodicity", contract.getSalary().getPeriodicity() != null ? contract.getSalary().getPeriodicity().name() : "N/A");
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

}