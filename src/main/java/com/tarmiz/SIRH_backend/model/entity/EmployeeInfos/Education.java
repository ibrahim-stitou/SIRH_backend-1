package com.tarmiz.SIRH_backend.model.entity.EmployeeInfos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;
    private String diploma;
    private Integer year;
    private String institution;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}

