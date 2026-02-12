package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
import com.tarmiz.SIRH_backend.enums.Contract.AmendmentStatus;
import com.tarmiz.SIRH_backend.enums.Contract.AmendmentType;
import org.springframework.data.jpa.domain.Specification;

public class AmendmentSpecifications {

    public static Specification<Amendment> hasStatus(AmendmentStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Amendment> hasTypeModification(AmendmentType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("typeModification"), type);
        };
    }

    public static Specification<Amendment> hasContractReference(String reference) {
        return (root, query, cb) -> {
            if (reference == null || reference.isBlank()) return null;
            return cb.like(
                    cb.lower(root.get("contract").get("reference")),
                    "%" + reference.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Amendment> hasObjetText(String objet) {
        return (root, query, cb) -> {
            if (objet == null || objet.isBlank()) return null;
            return cb.like(
                    cb.lower(root.get("objet")),
                    "%" + objet.toLowerCase() + "%"
            );
        };
    }
}
