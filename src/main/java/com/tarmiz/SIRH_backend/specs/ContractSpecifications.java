package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.enums.Contract.ContractStatusEnum;
import com.tarmiz.SIRH_backend.enums.Contract.ContractTypeEnum;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecifications {

    public static Specification<Contract> hasEmployeeId(Long employeeId) {
        return (root, query, cb) -> {
            if (employeeId == null) return null;
            return cb.equal(root.get("employee").get("id"), employeeId);
        };
    }

    public static Specification<Contract> hasType(ContractTypeEnum type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<Contract> hasStatus(ContractStatusEnum status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Contract> hasDepartmentId(Long departmentId) {
        return (root, query, cb) -> {
            if (departmentId == null) return null;
            return cb.equal(root.get("employee").get("department").get("id"), departmentId);
        };
    }
}
