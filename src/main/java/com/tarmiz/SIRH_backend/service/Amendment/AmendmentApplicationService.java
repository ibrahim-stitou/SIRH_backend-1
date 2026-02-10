package com.tarmiz.SIRH_backend.service.Amendment;

import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ContractJobRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ContractSalaryRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.ContractScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmendmentApplicationService {

    private final ContractSalaryRepository contractSalaryRepository;
    private final ContractScheduleRepository contractScheduleRepository;
    private final ContractJobRepository contractJobRepository;

    @Transactional
    public void applyLines(Amendment amendment) {

        Contract contract = amendment.getContract();

        switch (amendment.getTypeModification()) {

            case SALARY -> {
                contractSalaryRepository
                        .findByContractIdAndActiveTrue(contract.getId())
                        .forEach(s -> s.setActive(false));

                contractSalaryRepository.flush();

                contractSalaryRepository
                        .findByAmendmentId(amendment.getId())
                        .forEach(s -> s.setActive(true));
            }

            case SCHEDULE -> {
                contractScheduleRepository
                        .findByContractIdAndActiveTrue(contract.getId())
                        .forEach(s -> s.setActive(false));

                contractScheduleRepository.flush();

                contractScheduleRepository
                        .findByAmendmentId(amendment.getId())
                        .forEach(s -> s.setActive(true));
            }

            case JOB -> {
                contractJobRepository
                        .findByContractIdAndActiveTrue(contract.getId())
                        .forEach(j -> j.setActive(false));

                contractJobRepository.flush();

                contractJobRepository
                        .findByAmendmentId(amendment.getId())
                        .forEach(j -> j.setActive(true));
            }
        }
    }
}
