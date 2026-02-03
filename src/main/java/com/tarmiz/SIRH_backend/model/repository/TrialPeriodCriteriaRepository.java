package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Contract.TrialPeriodCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrialPeriodCriteriaRepository extends JpaRepository<TrialPeriodCriteria, Long> {
    long countByIdTrialCriteriaId(Long criteriaId);
}
