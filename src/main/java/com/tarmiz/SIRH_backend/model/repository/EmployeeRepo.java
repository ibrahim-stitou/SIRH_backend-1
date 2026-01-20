package com.tarmiz.SIRH_backend.model.repository;
import com.tarmiz.SIRH_backend.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByMatricule(String matricule);

    Optional<Employee> findByCin(String cin);
}
