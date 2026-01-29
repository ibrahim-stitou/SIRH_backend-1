package com.tarmiz.SIRH_backend.model.entity.Contract;


import com.tarmiz.SIRH_backend.enums.Contract.ContractConditionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trial_criteria")
public class TrialCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ContractConditionId id;

    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;
}

