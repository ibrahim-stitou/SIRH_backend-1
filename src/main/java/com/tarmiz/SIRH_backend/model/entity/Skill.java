package com.tarmiz.SIRH_backend.model.entity;

import com.tarmiz.SIRH_backend.enums.SkillLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String skillName;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
