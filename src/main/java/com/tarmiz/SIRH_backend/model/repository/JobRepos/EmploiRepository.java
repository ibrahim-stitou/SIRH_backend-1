package com.tarmiz.SIRH_backend.model.repository.JobRepos;

import com.tarmiz.SIRH_backend.model.entity.Job.Emploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploiRepository extends JpaRepository<Emploi, Long> {
    boolean existsByMetierIdAndCode(Long metierId, String code);
    @Query("SELECT e.id AS id, e.libelle AS libelle FROM Emploi e WHERE e.metier.id = :metierId")
    List<Object[]> findIdAndLibelleByMetierId(@Param("metierId") Long metierId);
    boolean existsByMetierId(Long metierId);
}