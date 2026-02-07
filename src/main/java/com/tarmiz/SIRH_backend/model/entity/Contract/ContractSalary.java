package com.tarmiz.SIRH_backend.model.entity.Contract;


import com.tarmiz.SIRH_backend.enums.Contract.PaymentMethodEnum;
import com.tarmiz.SIRH_backend.enums.Contract.PaymentPeriodicityEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "contract_salary")
public class ContractSalary extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @NotNull
    private Integer baseSalary;

    @NotNull
    private BigDecimal salaryBrut;

    @NotNull
    private BigDecimal salaryNet;

    @Column(nullable = false, updatable = false)
    private String currency = "MAD";

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentPeriodicityEnum periodicity;

    @OneToMany(mappedBy = "contractSalary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prime> primes;

    @Min(1)
    @Max(31)
    private Integer paymentDay;
}

