package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.enums.Contract.PrimeTypeIdEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "primes")
public class Prime extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_salary_id", nullable = false)
    private ContractSalary contractSalary;

    @Column(name = "prime_type_id")
    private PrimeTypeIdEnum primeTypeId;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "is_taxable", nullable = false)
    private Boolean isTaxable = true;

    @Column(name = "is_subject_to_cnss", nullable = false)
    private Boolean isSubjectToCnss = true;
}