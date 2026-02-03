package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Contract.Clause;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractClause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClauseRepository extends JpaRepository<Clause, Long> {
    boolean existsById(Long id);
}