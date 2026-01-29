package com.tarmiz.SIRH_backend.model.entity.EmployeeInfos;

import com.tarmiz.SIRH_backend.enums.Relationship;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "person_in_charge")
public class PersonInCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Relationship relationship;
    @Column(unique = true)
    private String cin;
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
