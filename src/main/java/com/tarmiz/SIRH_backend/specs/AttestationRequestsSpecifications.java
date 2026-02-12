package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.enums.AttestationDemandStatus;
import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.model.entity.Attestation.DemandeAttestation;
import org.springframework.data.jpa.domain.Specification;

public class AttestationRequestsSpecifications {
    public static Specification<DemandeAttestation> hasEmployeeId(Long employeeId) {
        return (root, query, cb) -> {
            if (employeeId == null) return null;
            return cb.equal(root.get("employee").get("id"), employeeId);
        };
    }

    public static Specification<DemandeAttestation> hasType(AttestationType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("typeAttestation"), type);
        };
    }

    public static Specification<DemandeAttestation> hasStatus(AttestationDemandStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }
}
