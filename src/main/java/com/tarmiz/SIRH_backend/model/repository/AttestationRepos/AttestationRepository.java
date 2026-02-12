package com.tarmiz.SIRH_backend.model.repository.AttestationRepos;

import com.tarmiz.SIRH_backend.model.entity.Attestation.Attestation;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AttestationRepository extends JpaRepository<Attestation, Long>, JpaSpecificationExecutor<Attestation> {

    @Query("""
    SELECT COUNT(a)
    FROM Attestation a
    WHERE EXTRACT(YEAR FROM a.dateGeneration) = :year
""")
    int countByYear(@Param("year") int year);

    Page<Attestation> findAllByDemandeAttestation_Employee_IdAndTypeAttestationAndNumeroAttestationContaining(
            Long employeeId,
            AttestationType typeAttestation,
            String numeroAttestation,
            Pageable pageable
    );

    Optional<Attestation> findById(Long id);

    Optional<Attestation> findByDemandeAttestation_Id(Long requestId);
}