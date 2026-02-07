package com.tarmiz.SIRH_backend.service.SettingsServices;

import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.TrialCriteriaUpdateDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.TrialCriteria;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.TrialCriteriaRepository;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.TrialPeriodCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrialCriteriaService {

    private final TrialCriteriaRepository trialCriteriaRepository;
    private final TrialPeriodCriteriaRepository trialPeriodCriteriaRepository;

    /* ================= LIST ================= */
    public List<TrialCriteria> listAll() {
        return trialCriteriaRepository.findAll();
    }

    /* ================= GET ONE ================= */
    public TrialCriteria getById(Long id) {
        return trialCriteriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trial criteria not found"));
    }

    /* ================= CREATE ================= */
    public TrialCriteria create(TrialCriteriaUpdateDTO dto) {
        TrialCriteria criteria = new TrialCriteria();
        criteria.setName(dto.getName());
        criteria.setDescription(dto.getDescription());
        return trialCriteriaRepository.save(criteria);
    }

    /* ================= UPDATE (NO UPSERT) ================= */
    public TrialCriteria update(Long id, TrialCriteriaUpdateDTO dto) {
        TrialCriteria criteria = trialCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Trial criteria not found with id " + id));

        long linkedCount = trialPeriodCriteriaRepository.countByIdTrialCriteriaId(id);
        if (linkedCount > 0) {
            throw new BusinessException("Impossible de modifier le critère car il est déjà utilisé par " + linkedCount + " période(s) d'essai");
        }

        criteria.setName(dto.getName());
        criteria.setDescription(dto.getDescription());
        return trialCriteriaRepository.save(criteria);
    }

    /* ================= DELETE ================= */
    public void delete(Long id) {
        TrialCriteria criteria = trialCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Trial criteria not found with id " + id));

        long linkedCount = trialPeriodCriteriaRepository.countByIdTrialCriteriaId(id);
        if (linkedCount > 0) {
            throw new BusinessException("Impossible de supprimer le critère car il est déjà utilisé par " + linkedCount + " période(s) d'essai");
        }

        trialCriteriaRepository.delete(criteria);
    }
}