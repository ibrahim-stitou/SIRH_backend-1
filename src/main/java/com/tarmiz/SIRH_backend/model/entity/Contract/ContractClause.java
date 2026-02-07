package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "contract_clauses")
public class ContractClause {

    @EmbeddedId
    private ContractClauseId id;

    @ManyToOne
    @MapsId("contractId")
    @JoinColumn(name = "contract_id")
    @JsonBackReference
    private Contract contract;

    @ManyToOne
    @MapsId("clauseId")
    @JoinColumn(name = "clause_id")
    private Clause clause;

    private Boolean isActive = true;

    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }

}