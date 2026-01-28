package com.tarmiz.SIRH_backend.util.Filtres;

import com.tarmiz.SIRH_backend.model.entity.Siege;
import org.springframework.data.jpa.domain.Specification;

public class SiegeSpecification {

    public static Specification<Siege> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Siege> cityContains(String city) {
        return (root, query, cb) ->
                city == null ? null : cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<Siege> countryContains(String country) {
        return (root, query, cb) ->
                country == null ? null : cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }
}