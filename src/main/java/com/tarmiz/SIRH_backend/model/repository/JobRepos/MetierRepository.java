package com.tarmiz.SIRH_backend.model.repository.JobRepos;

import com.tarmiz.SIRH_backend.model.entity.Job.Metier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetierRepository extends JpaRepository<Metier, Long> {
    boolean existsByCode(String code);
}