package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "contract_primes")
public class Prime extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_salary_id", nullable = false)
    private ContractSalary contractSalary;

    @Column(name = "prime_type_id")
    private String primeTypeId;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "is_taxable", nullable = false)
    private Boolean isTaxable = true;

    @Column(name = "is_subject_to_cnss", nullable = false)
    private Boolean isSubjectToCnss = true;

}

