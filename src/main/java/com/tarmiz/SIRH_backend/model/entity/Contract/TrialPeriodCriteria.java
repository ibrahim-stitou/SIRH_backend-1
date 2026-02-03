package com.tarmiz.SIRH_backend.model.entity.Contract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    public TrialPeriodCriteriaId() {}

    public TrialPeriodCriteriaId(Long trialPeriodId, Long trialCriteriaId) {
        this.trialPeriodId = trialPeriodId;
        this.trialCriteriaId = trialCriteriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrialPeriodCriteriaId that = (TrialPeriodCriteriaId) o;
        return Objects.equals(trialPeriodId, that.trialPeriodId) &&
                Objects.equals(trialCriteriaId, that.trialCriteriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trialPeriodId, trialCriteriaId);
    }
}
