package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {

    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Employee> hasMatricule(String matricule) {
        return (root, query, cb) -> {
            if (matricule == null || matricule.isEmpty()) return null;
            return cb.like(cb.lower(root.get("matricule")), "%" + matricule.toLowerCase() + "%");
        };
    }

    public static Specification<Employee> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Employee> hasContractType(String contractType) {
        return (root, query, cb) -> {
            if (contractType == null || contractType.isEmpty()) return null;

            var contractJoin = root.join("contracts", JoinType.LEFT);
            return cb.equal(contractJoin.get("type"), contractType);
        };
    }
}
