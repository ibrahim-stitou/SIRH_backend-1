package com.tarmiz.SIRH_backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "employee_id"),
                @UniqueConstraint(columnNames = "siege_id")
        })
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String postalCode;
    private String country;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "siege_id", unique = true)
    private Siege siege;
}