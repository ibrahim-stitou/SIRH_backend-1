package com.tarmiz.SIRH_backend.service.SettingsServices.JobServices;

import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.JobMappers.EmploiMapper;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDetailDTO;
import com.tarmiz.SIRH_backend.model.entity.Job.Emploi;
import com.tarmiz.SIRH_backend.model.entity.Job.Metier;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.EmploiRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.MetierRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.PosteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmploiService {

    private final EmploiRepository emploiRepo;
    private final PosteRepository posteRepo;
    private final EmploiMapper mapper;
    private final MetierRepository metierRepo;

    public List<EmploiDTO> listAll() {
        return emploiRepo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getEmploisByMetier(Long metierId) {
        List<Object[]> results = emploiRepo.findIdAndLibelleByMetierId(metierId);
        return results.stream()
                .map(r -> Map.of("id", r[0], "libelle", r[1]))
                .collect(Collectors.toList());
    }

    public EmploiDetailDTO getDetails(Long id) {
        Emploi emploi = emploiRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Emploi non trouvé avec id=" + id));
        return mapper.toDetailDTO(emploi);
    }

    public EmploiDTO create(EmploiDTO dto, Long metierId) {
        if (emploiRepo.existsByMetierIdAndCode(metierId, dto.getCode()))
            throw new BusinessException("Code déjà utilisé pour cet emploi dans le métier id=" + metierId);
        Metier metier = metierRepo.findById(metierId)
                .orElseThrow(() -> new BusinessException("Métier non trouvé avec id=" + metierId));
        Emploi emploi = new Emploi();
        emploi.setCode(dto.getCode());
        emploi.setLibelle(dto.getLibelle());
        emploi.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
        emploi.setMetier(metier);
        return mapper.toDTO(emploiRepo.save(emploi));
    }

    public EmploiDTO update(Long id, EmploiDTO dto) {
        Emploi emploi = emploiRepo.findById(dto.getId())
                .orElseThrow(() -> new BusinessException("Emploi non trouvé avec id=" + id));
        emploi.setStatut(dto.getStatut());
        return mapper.toDTO(emploiRepo.save(emploi));
    }

    public void delete(Long id) {
        if (posteRepo.existsByEmploiId(id))
            throw new BusinessException("Impossible de supprimer cet emploi : postes associés existants");
        emploiRepo.deleteById(id);
    }

    public void activate(Long id) {
        Emploi emploi = emploiRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Emploi non trouvé avec id=" + id));
        emploi.setStatut("actif");
        emploiRepo.save(emploi);
    }

    public void deactivate(Long id) {
        Emploi emploi = emploiRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Emploi non trouvé avec id=" + id));
        emploi.setStatut("inactif");
        emploi.getPostes().forEach(p -> p.setStatut("inactif"));
        emploiRepo.save(emploi);
    }
}