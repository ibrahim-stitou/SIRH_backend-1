package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
}
