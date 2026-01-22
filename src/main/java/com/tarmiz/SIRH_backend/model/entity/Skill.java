package com.tarmiz.SIRH_backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String skillName;

    @Column(name = "skill_level", nullable = false)
    private int skillLevel;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


}
