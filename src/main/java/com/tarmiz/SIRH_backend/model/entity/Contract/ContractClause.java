package com.tarmiz.SIRH_backend.model.entity.Contract;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract_clauses")
public class ContractClause {

    @EmbeddedId
    private ContractClauseId id;

    @ManyToOne
    @MapsId("contractId")
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @MapsId("clauseId")
    @JoinColumn(name = "clause_id")
    private Clause clause;

    private Boolean isActive = true;

    private LocalDateTime addedAt = LocalDateTime.now();

}

@Embeddable
class ContractClauseId implements java.io.Serializable {

    private Long contractId;
    private Long clauseId;

    // hashCode, equals
}

