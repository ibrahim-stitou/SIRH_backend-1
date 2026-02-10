package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractScheduleRepository extends JpaRepository<ContractSchedule, Long> {
    List<ContractSchedule> findByContractIdAndActiveTrue(Long contractId);
    List<ContractSchedule> findByAmendmentId(Long amendmentId);
}
