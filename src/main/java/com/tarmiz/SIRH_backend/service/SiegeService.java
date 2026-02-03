package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.exception.BusinessException.SiegeDeletionForbiddenException;
import com.tarmiz.SIRH_backend.mapper.SiegeMapper;
import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeListDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Company;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import com.tarmiz.SIRH_backend.model.repository.CompanyRepository;
import com.tarmiz.SIRH_backend.model.repository.GroupRepository;
import com.tarmiz.SIRH_backend.model.repository.SiegeRepository;
import com.tarmiz.SIRH_backend.util.Filtres.SiegeSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SiegeService {

    @Autowired
    private SiegeRepository siegeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SiegeMapper siegeMapper;

    public ApiListResponse<SiegeListDTO> getSieges(Integer start, Integer length, String dir,
                                                   String name, String city, String country) {
        Sort sort = Sort.by("createdAt");
        sort = "asc".equalsIgnoreCase(dir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(start / length, length, sort);

        Specification<Siege> spec = Specification.where(SiegeSpecification.nameContains(name))
                .and(SiegeSpecification.cityContains(city))
                .and(SiegeSpecification.countryContains(country));

        Page<Siege> page = siegeRepository.findAll(spec, pageable);

        List<SiegeListDTO> dtos = siegeMapper.toDTOList(page.getContent());

        ApiListResponse<SiegeListDTO> response = new ApiListResponse<>();
        response.setStatus("success");
        response.setMessage("Récupération réussie");
        response.setData(dtos);
        response.setRecordsTotal(siegeRepository.count());
        response.setRecordsFiltered(page.getTotalElements());

        return response;
    }

    public SiegeListDTO getSiegeDetails(Long siegeId) {
        Siege siege = siegeRepository.findById(siegeId)
                .orElseThrow(() -> new IllegalArgumentException("Siege not found with id: " + siegeId));

        return siegeMapper.toListDTO(siege);
    }

    public SiegeListDTO createSiege(SiegeCreateDTO dto) {
        Siege siege = siegeMapper.toEntity(dto);
        Company defaultCompany = companyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default company not found"));

        siege.setCompany(defaultCompany);
        siege.setCreatedAt(LocalDate.now());
        siege.setUpdatedAt(LocalDate.now());

        Siege saved = siegeRepository.save(siege);

        return siegeMapper.toListDTO(saved);
    }

    /* ================= Delete Siege ================= */
    @Transactional
    public void deleteSiege(Long siegeId) {
        Siege siege = siegeRepository.findById(siegeId).orElse(null);

        boolean hasActiveGroups = groupRepository.existsBySiegeId(siegeId);
        if (hasActiveGroups) {
            throw new SiegeDeletionForbiddenException(siegeId);
        }

        //siege.setDeletedAt(LocalDateTime.now());
        siegeRepository.save(siege);
    }
}
