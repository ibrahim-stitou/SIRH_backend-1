package com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos;

import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Group;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    List<Group> findBySiege(Siege siege);
    List<Group> findByEmployees_Id(Long employeeId);
    boolean existsBySiegeId(Long siegeId);
    @Query("""
        SELECT g
        FROM Group g
        JOIN FETCH g.manager m
        LEFT JOIN FETCH m.department d
        WHERE g.manager IS NOT NULL
    """)
    List<Group> findAllGroupsWithManagers();
}
