package com.tarmiz.SIRH_backend.model.repository.AttestationRepos;

import com.tarmiz.SIRH_backend.model.entity.Attestation.DemandeAttestation;
import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandeAttestationRepository extends JpaRepository<DemandeAttestation, Long>,
        JpaSpecificationExecutor<DemandeAttestation> {

    Page<DemandeAttestation> findAllByEmployee_IdAndTypeAttestationAndStatus(
            Long employeeId,
            AttestationType typeAttestation,
            AttestationDemandStatus status,
            Pageable pageable
    );

    Optional<DemandeAttestation> findById(Long id);
}