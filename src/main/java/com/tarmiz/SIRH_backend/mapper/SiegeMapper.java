package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.model.DTO.SiegeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.SiegeListDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Address;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Siege;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SiegeMapper {

    /** ================= Map List of Entities ================= */
    public List<SiegeListDTO> toDTOList(List<Siege> sieges) {
        if (sieges == null) return null;
        return sieges.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    /** ================= Create Mapping ================= */
    public Siege toEntity(SiegeCreateDTO dto) {
        if (dto == null) return null;

        Siege siege = new Siege();
        siege.setName(dto.getName());
        siege.setCode(dto.getCode());
        siege.setPhone(dto.getPhone());
        siege.setEmail(dto.getEmail());
        siege.setCreatedAt(LocalDate.now());
        siege.setUpdatedAt(LocalDate.now());

        // Address
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        address.setSiege(siege);
        siege.setAddress(address);

        return siege;
    }

    /** ================= Create DTO from Entity ================= */
    public SiegeCreateDTO toCreateDTO(Siege siege) {
        if (siege == null) return null;

        SiegeCreateDTO dto = new SiegeCreateDTO();
        dto.setName(siege.getName());
        dto.setCode(siege.getCode());
        dto.setStreet(siege.getAddress() != null ? siege.getAddress().getStreet() : null);
        dto.setCity(siege.getAddress() != null ? siege.getAddress().getCity() : null);
        dto.setCountry(siege.getAddress() != null ? siege.getAddress().getCountry() : null);
        dto.setPhone(siege.getPhone());
        dto.setEmail(siege.getEmail());
        dto.setEmail(null);
        return dto;
    }

    /** ================= List Mapping ================= */
    public SiegeListDTO toListDTO(Siege siege) {
        if (siege == null) return null;

        SiegeListDTO dto = new SiegeListDTO();
        dto.setId(siege.getId());
        dto.setName(siege.getName());
        dto.setCode(siege.getCode());
        dto.setPhone(siege.getPhone());
        dto.setAddress(siege.getAddress() != null ? siege.getAddress().getStreet() : null);
        dto.setCity(siege.getAddress() != null ? siege.getAddress().getCity() : null);
        dto.setCountry(siege.getAddress() != null ? siege.getAddress().getCountry() : null);
        dto.setGroupsCount(siege.getGroups() != null ? siege.getGroups().size() : 0);
        dto.setGroups(siege.getGroups() != null
                ? siege.getGroups().stream()
                .map(g -> {
                    SiegeListDTO.GroupInfo groupInfo = new SiegeListDTO.GroupInfo();
                    groupInfo.setId(g.getId());
                    groupInfo.setName(g.getName());
                    groupInfo.setCode(g.getCode());
                    return groupInfo;
                })
                .collect(Collectors.toList())
                : null);
        dto.setCreatedAt(siege.getCreatedAt());
        dto.setUpdatedAt(siege.getUpdatedAt());

        return dto;
    }
}
