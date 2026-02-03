package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "trial_periods")
public class TrialPeriod extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private Boolean enabled = true;

    private Integer durationMonths = 0;
    private Integer durationDays = 0;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean renewable = false;
    private Integer maxRenewals = 0;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @OneToMany(mappedBy = "trialPeriod", cascade = CascadeType.ALL)
    private List<TrialPeriodCriteria> trialPeriodCriteriaList;
}