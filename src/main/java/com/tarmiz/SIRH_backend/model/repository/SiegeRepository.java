package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SiegeRepository extends JpaRepository<Siege, Long>, JpaSpecificationExecutor<Siege> {
}