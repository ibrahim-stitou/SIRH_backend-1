package com.tarmiz.SIRH_backend.model.entity;

import com.tarmiz.SIRH_backend.enums.Relationship;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "persons_in_charge")
public class PersonInCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Relationship relationship;
    private String cin;
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
