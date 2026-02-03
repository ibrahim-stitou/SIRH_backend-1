package com.tarmiz.SIRH_backend.service.SettingsServices.JobServices;

import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.JobMappers.MetierMapper;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDetailDTO;
import com.tarmiz.SIRH_backend.model.entity.Job.Metier;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.EmploiRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.MetierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetierService {

    private final MetierRepository metierRepo;
    private final EmploiRepository emploiRepo;
    private final MetierMapper mapper;

    public List<MetierDTO> listAll() {
        return metierRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllMetiersSimplified() {
        return metierRepo.findAll().stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", m.getId());
                    map.put("libelle", m.getLibelle());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public MetierDetailDTO getDetails(Long id) {
        Metier metier = metierRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Métier non trouvé avec id=" + id));
        return mapper.toDetailDTO(metier);
    }

    public MetierDTO create(MetierDTO dto) {
        if (metierRepo.existsByCode(dto.getCode())) {
            throw new BusinessException("Code déjà utilisé pour un métier : " + dto.getCode());
        }
        Metier metier = new Metier();
        metier.setCode(dto.getCode());
        metier.setLibelle(dto.getLibelle());
        metier.setDomaine(dto.getDomaine());
        metier.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
        return mapper.toDTO(metierRepo.save(metier));
    }

    public MetierDTO update(Long id, MetierDTO dto) {
        Metier metier = metierRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Métier non trouvé avec id=" + id));
        metier.setDomaine(dto.getDomaine());
        metier.setStatut(dto.getStatut());
        return mapper.toDTO(metierRepo.save(metier));
    }

    public void delete(Long id) {
        if (emploiRepo.existsByMetierId(id)) {
            throw new BusinessException("Impossible de supprimer ce métier : emplois associés existants");
        }
        metierRepo.deleteById(id);
    }

    public void activate(Long id) {
        Metier metier = metierRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Métier non trouvé avec id=" + id));
        metier.setStatut("actif");
        metierRepo.save(metier);
    }

    public void deactivate(Long id) {
        Metier metier = metierRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Métier non trouvé avec id=" + id));
        metier.setStatut("inactif");
        if (metier.getEmplois() != null) {
            metier.getEmplois().forEach(emploi -> {
                emploi.setStatut("inactif");
                if (emploi.getPostes() != null) {
                    emploi.getPostes().forEach(poste -> poste.setStatut("inactif"));
                }
            });
        }
        metierRepo.save(metier);
    }
}