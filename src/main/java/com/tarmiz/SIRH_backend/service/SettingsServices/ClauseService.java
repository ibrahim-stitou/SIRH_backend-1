package com.tarmiz.SIRH_backend.service.SettingsServices;


import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.ClauseUpdateDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Clause;
import com.tarmiz.SIRH_backend.model.repository.ClauseRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractClauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClauseService {
    private final ClauseRepository clauseRepository;
    private final ContractClauseRepository contractClauseRepository;

    /* ================= GET LIST ================= */
    public List<Clause> listAll() {
        return clauseRepository.findAll();
    }
    /* ================= CREATE ================= */
    public Clause create(ClauseUpdateDTO dto) {
        Clause clause = new Clause();
        clause.setName(dto.getName());
        clause.setDescription(dto.getDescription());
        return clauseRepository.save(clause);
    }

    /* ================= UPDATE ================= */
    public Clause update(Long id, ClauseUpdateDTO dto) {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Clause not found with id " + id));

        long linkedCount = contractClauseRepository.countByIdClauseId(id);
        if (linkedCount > 0) {
            throw new BusinessException("Impossible de modifier la clause car elle est déjà utilisée par " + linkedCount + " contrat(s)");
        }

        clause.setName(dto.getName());
        clause.setDescription(dto.getDescription());
        return clauseRepository.save(clause);
    }

    /* ================= DELETE ================= */
    public void delete(Long id) {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Clause not found with id " + id));

        long linkedCount = contractClauseRepository.countByIdClauseId(id);
        if (linkedCount > 0) {
            throw new BusinessException("Impossible de supprimer la clause car elle est déjà utilisée par " + linkedCount + " contrat(s)");
        }

        clauseRepository.delete(clause);
    }
}
