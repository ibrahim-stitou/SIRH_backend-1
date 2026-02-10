package com.tarmiz.SIRH_backend.model.repository.ContractRepos;

import com.tarmiz.SIRH_backend.enums.Contract.AmendmentStatus;
import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AmendmentRepository extends JpaRepository<Amendment, Long> {

    @Query("""
        SELECT COALESCE(MAX(a.numero), 0)
        FROM Amendment a
        WHERE a.contract.id = :contractId
    """)
    int findMaxNumeroByContractId(@Param("contractId") Long contractId);

    List<Amendment> findByContractIdOrderByAmendmentDateDesc(Long ContractId);
    List<Amendment> findByStatusAndEffectiveDateLessThanEqual(AmendmentStatus status, LocalDate today);
}
