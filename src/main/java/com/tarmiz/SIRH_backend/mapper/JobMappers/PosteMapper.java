package com.tarmiz.SIRH_backend.mapper.JobMappers;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDetailDTO;
import com.tarmiz.SIRH_backend.model.entity.Job.Poste;
import org.springframework.stereotype.Component;

@Component
public class PosteMapper {

    public PosteDTO toDTO(Poste poste) {
        PosteDTO dto = new PosteDTO();
        dto.setId(poste.getId());
        dto.setCode(poste.getCode());
        dto.setLibelle(poste.getLibelle());
        dto.setStatut(poste.getStatut());
        return dto;
    }

    public PosteDetailDTO toDetailDTO(Poste poste) {
        PosteDetailDTO dto = new PosteDetailDTO();
        dto.setId(poste.getId());
        dto.setCode(poste.getCode());
        dto.setLibelle(poste.getLibelle());
        dto.setStatut(poste.getStatut());
        dto.setDepartmentId(poste.getDepartment() != null ? poste.getDepartment().getId() : null);
        if (poste.getEmploi() != null) {
            EmploiDTO emploiDto = new EmploiDTO();
            emploiDto.setId(poste.getEmploi().getId());
            emploiDto.setCode(poste.getEmploi().getCode());
            emploiDto.setLibelle(poste.getEmploi().getLibelle());
            emploiDto.setStatut(poste.getEmploi().getStatut());
            dto.setEmploi(emploiDto);
            if (poste.getEmploi().getMetier() != null) {
                MetierDTO metierDto = new MetierDTO();
                metierDto.setId(poste.getEmploi().getMetier().getId());
                metierDto.setCode(poste.getEmploi().getMetier().getCode());
                metierDto.setLibelle(poste.getEmploi().getMetier().getLibelle());
                metierDto.setDomaine(poste.getEmploi().getMetier().getDomaine());
                metierDto.setStatut(poste.getEmploi().getMetier().getStatut());
                dto.setMetier(metierDto);
            }
        }
        return dto;
    }
}