package com.tarmiz.SIRH_backend.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String nameAr;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();
}

