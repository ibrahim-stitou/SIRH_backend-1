package com.tarmiz.SIRH_backend.model.entity.Contract;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ContractClauseId implements Serializable {
    private Long contractId;
    private Long clauseId;

    public ContractClauseId() {}

    public ContractClauseId(Long contractId, Long clauseId) {
        this.contractId = contractId;
        this.clauseId = clauseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractClauseId)) return false;
        ContractClauseId that = (ContractClauseId) o;
        return Objects.equals(contractId, that.contractId)
                && Objects.equals(clauseId, that.clauseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractId, clauseId);
    }
}