package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.model.entity.Contract.TrialCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrialCriteriaRepository extends JpaRepository<TrialCriteria, Long> {
    boolean existsById(Long id);
}