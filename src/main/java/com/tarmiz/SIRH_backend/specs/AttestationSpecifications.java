package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.enums.AttestationType;
import com.tarmiz.SIRH_backend.model.entity.Attestation.Attestation;
import org.springframework.data.jpa.domain.Specification;

public class AttestationSpecifications {

    public static Specification<Attestation> hasEmployeeId(Long employeeId) {
        return (root, query, cb) -> {
            if (employeeId == null) return null;
            return cb.equal(root.get("demandeAttestation").get("employee").get("id"), employeeId);
        };
    }

    public static Specification<Attestation> hasType(AttestationType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("typeAttestation"), type);
        };
    }

    public static Specification<Attestation> hasNumero(String numero) {
        return (root, query, cb) -> {
            if (numero == null || numero.isEmpty()) return null;
            return cb.like(cb.lower(root.get("numeroAttestation")), "%" + numero.toLowerCase() + "%");
        };
    }
}