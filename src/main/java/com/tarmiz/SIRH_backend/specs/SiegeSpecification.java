package com.tarmiz.SIRH_backend.specs;

import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Address;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class SiegeSpecification {

    public static Specification<Siege> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Siege> codeContains(String code) {
        return (root, query, cb) -> {
            if (code == null || code.isBlank()) return null;
            return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<Siege> cityContains(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) return null;
            Join<Siege, Address> addressJoin = root.join("address", JoinType.LEFT);
            return cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%");
        };
    }

    public static Specification<Siege> countryContains(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isBlank()) return null;
            Join<Siege, Address> addressJoin = root.join("address", JoinType.LEFT);
            return cb.like(cb.lower(addressJoin.get("country")), "%" + country.toLowerCase() + "%");
        };
    }
}