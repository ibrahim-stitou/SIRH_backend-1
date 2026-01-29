package com.tarmiz.SIRH_backend.model.entity.Contract;

import jakarta.persistence.*;

@Entity
@Table(name = "clauses")
public class Clause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Getters and setters
}

