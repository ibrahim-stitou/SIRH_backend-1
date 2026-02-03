package com.tarmiz.SIRH_backend.model.repository.JobRepos;

import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosteRepository extends JpaRepository<Poste, Long> {
    boolean existsByEmploiIdAndCode(Long emploiId, String code);
    boolean existsByEmploiId(Long emploiId);
    boolean existsByContractJobs_Id(Long contractJobId);

    @Query("SELECT p.id AS id, p.libelle AS libelle FROM Poste p WHERE p.emploi.id = :emploiId")
    List<Object[]> findIdAndLibelleByEmploiId(@Param("emploiId") Long emploiId);

}