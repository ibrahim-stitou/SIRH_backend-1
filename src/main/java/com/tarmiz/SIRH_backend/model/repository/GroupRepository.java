package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Group;
import com.tarmiz.SIRH_backend.model.entity.Siege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    List<Group> findBySiege(Siege siege);
    List<Group> findByEmployees_Id(Long employeeId);
}
