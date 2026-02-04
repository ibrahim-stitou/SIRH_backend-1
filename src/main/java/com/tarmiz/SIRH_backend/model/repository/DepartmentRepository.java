package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.enums.DepartmentStatus;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByStatus(DepartmentStatus status);
    boolean existsByIdAndEmployeesIsNotEmpty(Long id);
}