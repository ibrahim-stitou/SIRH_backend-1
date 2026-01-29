package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.enums.Contract.WorkLevelEnum;
import com.tarmiz.SIRH_backend.enums.Contract.WorkModeEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "contract_job")
public class ContractJob extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private String metier;
    private String emploi;
    private String poste;

    @Enumerated(EnumType.STRING)
    private WorkModeEnum workMode;

    private String classification;

    @Enumerated(EnumType.STRING)
    private String workLocation;

    @Enumerated(EnumType.STRING)
    private WorkLevelEnum level;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

}

