package com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String nameAr;

    private String phone;

    @Email
    private String email;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Siege> sieges;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Department> departments;
}