package com.tarmiz.SIRH_backend.model.entity.Contract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trial_period_criteria")
public class TrialPeriodCriteria {

    @EmbeddedId
    private TrialPeriodCriteriaId id;

    @ManyToOne
    @MapsId("trialPeriodId")
    @JoinColumn(name = "trial_period_id")
    private TrialPeriod trialPeriod;

    @ManyToOne
    @MapsId("trialCriteriaId")
    @JoinColumn(name = "trial_criteria_id")
    private TrialCriteria trialCriteria;
}

@Embeddable
class TrialPeriodCriteriaId implements java.io.Serializable {

    private Long trialPeriodId;
    private Long trialCriteriaId;

    // hashCode, equals
}
