package com.tarmiz.SIRH_backend.mapper.JobMappers;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDetailDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDTO;
import com.tarmiz.SIRH_backend.model.entity.Job.Emploi;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EmploiMapper {

    public EmploiDTO toDTO(Emploi emploi) {
        EmploiDTO dto = new EmploiDTO();
        dto.setId(emploi.getId());
        dto.setCode(emploi.getCode());
        dto.setLibelle(emploi.getLibelle());
        dto.setStatut(emploi.getStatut());
        return dto;
    }

    public EmploiDetailDTO toDetailDTO(Emploi emploi) {
        EmploiDetailDTO dto = new EmploiDetailDTO();
        dto.setId(emploi.getId());
        dto.setCode(emploi.getCode());
        dto.setLibelle(emploi.getLibelle());
        dto.setStatut(emploi.getStatut());
        if (emploi.getMetier() != null) {
            MetierDTO metierDto = new MetierDTO();
            metierDto.setId(emploi.getMetier().getId());
            metierDto.setCode(emploi.getMetier().getCode());
            metierDto.setLibelle(emploi.getMetier().getLibelle());
            metierDto.setDomaine(emploi.getMetier().getDomaine());
            metierDto.setStatut(emploi.getMetier().getStatut());
            dto.setMetier(metierDto);
        }
        if (emploi.getPostes() != null) {
            dto.setPostes(emploi.getPostes().stream().map(p -> {
                PosteDTO pDto = new PosteDTO();
                pDto.setId(p.getId());
                pDto.setCode(p.getCode());
                pDto.setLibelle(p.getLibelle());
                pDto.setStatut(p.getStatut());
                return pDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
