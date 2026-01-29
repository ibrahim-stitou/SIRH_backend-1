package com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy;

import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String nameAr;

    @Column(length = 50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(name = "manager_id", unique = true)
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "siege_id", nullable = false)
    private Siege siege;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Employee> employees;

}