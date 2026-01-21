package com.tarmiz.SIRH_backend.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
