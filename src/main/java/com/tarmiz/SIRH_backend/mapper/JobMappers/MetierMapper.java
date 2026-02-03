package com.tarmiz.SIRH_backend.mapper.JobMappers;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDetailDTO;
import com.tarmiz.SIRH_backend.model.entity.Job.Metier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MetierMapper {

    private final EmploiMapper emploiMapper;

    public MetierMapper(EmploiMapper emploiMapper) {
        this.emploiMapper = emploiMapper;
    }

    public MetierDTO toDTO(Metier metier) {
        MetierDTO dto = new MetierDTO();
        dto.setId(metier.getId());
        dto.setCode(metier.getCode());
        dto.setLibelle(metier.getLibelle());
        dto.setDomaine(metier.getDomaine());
        dto.setStatut(metier.getStatut());
        return dto;
    }

    public MetierDetailDTO toDetailDTO(Metier metier) {
        MetierDetailDTO dto = new MetierDetailDTO();
        dto.setId(metier.getId());
        dto.setCode(metier.getCode());
        dto.setLibelle(metier.getLibelle());
        dto.setDomaine(metier.getDomaine());
        dto.setStatut(metier.getStatut());
        if (metier.getEmplois() != null) {
            dto.setEmplois(metier.getEmplois().stream()
                    .map(emploiMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
