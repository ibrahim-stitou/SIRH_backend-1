package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {
            "educationList",
            "skills",
            "certifications",
            "experiences",
            "emergencyContacts",
            "address",
            "department"
    })
    Optional<Employee> findById(Long id);
    Page<Employee> findAll(Pageable pageable);
}