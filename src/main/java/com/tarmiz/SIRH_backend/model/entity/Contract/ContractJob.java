package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.enums.Contract.WorkLevelEnum;
import com.tarmiz.SIRH_backend.enums.Contract.WorkModeEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "contract_job")
public class ContractJob extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "poste_id", nullable = false)
    private Poste poste;

    @Enumerated(EnumType.STRING)
    private WorkModeEnum workMode;

    private String classification;

    @Enumerated(EnumType.STRING)
    private WorkLevelEnum level;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @ManyToOne
    @JoinColumn(name = "amendment_id")
    private Amendment amendment;

    private LocalDate effectiveDate;

    @Column(nullable = false)
    private Boolean active = true;

}