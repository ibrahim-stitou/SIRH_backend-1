package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.ContractSalary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractSalaryRepository extends JpaRepository<ContractSalary, Long> {
    List<ContractSalary> findByContractIdAndActiveTrue(Long contractId);
    List<ContractSalary> findByAmendmentId(Long amendmentId);
}
