package com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos;

import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
