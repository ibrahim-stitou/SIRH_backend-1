package com.tarmiz.SIRH_backend.model.entity.Contract;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tarmiz.SIRH_backend.enums.Contract.ContractStatusEnum;
import com.tarmiz.SIRH_backend.enums.Contract.ContractTypeEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contracts",
        uniqueConstraints = {@UniqueConstraint(columnNames = "reference")})
public class Contract extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String reference;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContractTypeEnum type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContractStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private LocalDate signatureDate;

    @NotNull
    private LocalDate startDate;

    private LocalDate effectiveStartDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractSalary> salaries;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractJob> jobs;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractSchedule> schedules;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    private TrialPeriod trialPeriod;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ContractClause> contractClauses;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Amendment> amendments;
}