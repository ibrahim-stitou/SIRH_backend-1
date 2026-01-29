package com.tarmiz.SIRH_backend.model.entity.EmployeeInfos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String issuer;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
