package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

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
//    @Query("""
//        SELECT e FROM Employee e
//        WHERE (:name IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
//                         OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
//          AND (:matricule IS NULL OR LOWER(e.matricule) LIKE LOWER(CONCAT('%', :matricule, '%')))
//          AND (:contractType IS NULL OR e.contractType = :contractType)
//          AND (:status IS NULL OR e.status = :status)
//    """)
//    Page<Employee> findAllWithFilters(
//            @Param("name") String name,
//            @Param("matricule") String matricule,
//            @Param("contractType") String contractType,
//            @Param("status") String status,
//            Pageable pageable
//    );
    Page<Employee> findAll(Pageable pageable);
    void deleteById(Long id);
    boolean existsByGroupId(Long groupId);
}