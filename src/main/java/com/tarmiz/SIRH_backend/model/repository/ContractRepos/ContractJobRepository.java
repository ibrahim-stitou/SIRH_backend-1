package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.ContractJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractJobRepository extends JpaRepository<ContractJob, Long> {
    List<ContractJob> findByContractIdAndActiveTrue(Long contractId);
    List<ContractJob> findByAmendmentId(Long amendmentId);
}
