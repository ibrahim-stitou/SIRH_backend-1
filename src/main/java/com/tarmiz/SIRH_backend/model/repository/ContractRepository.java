package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
