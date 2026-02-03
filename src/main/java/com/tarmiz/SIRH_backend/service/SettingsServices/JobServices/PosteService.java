package com.tarmiz.SIRH_backend.service.SettingsServices.JobServices;

import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.JobMappers.PosteMapper;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDetailDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.model.entity.Job.Emploi;
import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import com.tarmiz.SIRH_backend.model.repository.DepartmentRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.EmploiRepository;
import com.tarmiz.SIRH_backend.model.repository.JobRepos.PosteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosteService {

    private final PosteRepository posteRepo;
    private final EmploiRepository emploiRepo;
    private final DepartmentRepository departmentRepo;
    private final PosteMapper mapper;

    public List<PosteDTO> listAll() {
        return posteRepo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPostesByEmploi(Long emploiId) {
        List<Object[]> results = posteRepo.findIdAndLibelleByEmploiId(emploiId);
        return results.stream()
                .map(r -> Map.of("id", r[0], "libelle", r[1]))
                .collect(Collectors.toList());
    }

    public PosteDetailDTO getDetails(Long id) {
        Poste poste = posteRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Poste non trouvé avec id=" + id));
        return mapper.toDetailDTO(poste);
    }

    public PosteDTO create(PosteDTO dto, Long emploiId, Long deptId) {

        if (posteRepo.existsByEmploiIdAndCode(emploiId, dto.getCode())) {
            throw new BusinessException(
                    "Code déjà utilisé pour ce poste dans l'emploi id=" + emploiId
            );
        }

        Emploi emploi = emploiRepo.findById(emploiId)
                .orElseThrow(() ->
                        new BusinessException("Emploi non trouvé avec id=" + emploiId)
                );

        Department department = departmentRepo.findById(deptId)
                .orElseThrow(() ->
                        new BusinessException("Département non trouvé avec id=" + deptId)
                );

        Poste poste = new Poste();
        poste.setCode(dto.getCode());
        poste.setLibelle(dto.getLibelle());
        poste.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
        poste.setEmploi(emploi);
        poste.setDepartment(department);

        return mapper.toDTO(posteRepo.save(poste));
    }

    public PosteDTO update(Long id, PosteDTO dto) {
        Poste poste = posteRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Poste non trouvé avec id=" + id));
        poste.setStatut(dto.getStatut());
        return mapper.toDTO(posteRepo.save(poste));
    }

    public void delete(Long id) {
        if (posteRepo.existsByContractJobs_Id(id))
            throw new BusinessException("Impossible de supprimer ce poste : lié à un contrat");
        posteRepo.deleteById(id);
    }

    public void activate(Long id) {
        Poste poste = posteRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Poste non trouvé avec id=" + id));
        poste.setStatut("actif");
        posteRepo.save(poste);
    }

    public void deactivate(Long id) {
        Poste poste = posteRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Poste non trouvé avec id=" + id));
        poste.setStatut("inactif");
        posteRepo.save(poste);
    }
}
