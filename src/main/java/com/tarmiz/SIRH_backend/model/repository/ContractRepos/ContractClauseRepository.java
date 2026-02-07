package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.ContractClause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractClauseRepository extends JpaRepository<ContractClause, Long> {
    long countByIdClauseId(Long clauseId);
    void deleteByContractId(Long contractId);
}
