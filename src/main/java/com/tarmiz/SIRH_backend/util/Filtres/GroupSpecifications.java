package com.tarmiz.SIRH_backend.util.Filtres;

import com.tarmiz.SIRH_backend.model.entity.Group;
import org.springframework.data.jpa.domain.Specification;

public class GroupSpecifications {

    public static Specification<Group> nameContains(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Group> codeContains(String code) {
        return (root, query, cb) -> code == null ? null :
                cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    public static Specification<Group> managerIdEquals(Long managerId) {
        return (root, query, cb) -> managerId == null ? null :
                cb.equal(root.get("manager").get("id"), managerId);
    }

    public static Specification<Group> siegeIdEquals(Long siegeId) {
        return (root, query, cb) -> siegeId == null ? null :
                cb.equal(root.get("siege").get("id"), siegeId);
    }
}
