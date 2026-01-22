package com.tarmiz.SIRH_backend.model.entity;

import com.tarmiz.SIRH_backend.enums.AptitudeMedical;
import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.enums.MaritalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "matricule"),
                @UniqueConstraint(columnNames = "cin")
        }
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String matricule;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String firstNameAr;
    private String lastNameAr;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @Column(nullable = false)
    private String cin;

    private LocalDate birthDate;
    private String birthPlace;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer numberOfChildren;

    private String phone;

    @Email
    @Column(nullable = false)
    private String email;

    private String aptitudeMedical;

    private String bankName;
    private String rib;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

    /* ================= Relations ================= */

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonInCharge> emergencyContacts = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educationList = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();
}