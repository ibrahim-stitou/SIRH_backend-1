package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.model.entity.Attestation;
import com.tarmiz.SIRH_backend.model.entity.DemandeAttestation;
import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AttestationRepository extends JpaRepository<Attestation, Long> {

    Page<Attestation> findAllByDemandeAttestation_Employee_IdAndTypeAttestationAndNumeroAttestationContaining(
            Long employeeId,
            AttestationType typeAttestation,
            String numeroAttestation,
            Pageable pageable
    );

    Optional<Attestation> findById(Long id);

    Optional<Attestation> findByDemandeAttestation_Id(Long requestId);
}