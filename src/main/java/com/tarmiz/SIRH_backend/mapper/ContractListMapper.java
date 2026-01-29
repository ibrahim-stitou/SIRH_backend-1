package com.tarmiz.SIRH_backend.mapper;

import com.tarmiz.SIRH_backend.enums.Contract.ContractConditionId;
import com.tarmiz.SIRH_backend.model.DTO.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ContractListMapper {

    @Mapping(target = "id", expression = "java(contract.getReference())")
    @Mapping(target = "employeId", expression = "java(contract.getEmployee().getMatricule())")
    @Mapping(target = "employeeName", expression = "java(contract.getEmployee().getFirstName() + \" \" + contract.getEmployee().getLastName())")
    @Mapping(target = "employeeMatricule", expression = "java(contract.getEmployee().getMatricule())")
    @Mapping(target = "dates", source = "contract", qualifiedByName = "mapDates")
    @Mapping(target = "job", source = "contract.contractJob")
    @Mapping(target = "schedule", source = "contract.contractSchedules")
    @Mapping(target = "salary", source = "contract.contractSalary")
    @Mapping(target = "conditions.selected", expression = "java(contract.getTrialPeriod() != null ? contract.getTrialPeriod().getCriteria().stream().map(c -> c.getLabel()).collect(Collectors.toList()) : null)")
    @Mapping(target = "signedDocument", expression = "java(mapSignedDocument(contract))")
    @Mapping(target = "legal", expression = "java(mapLegalStatic())")
    @Mapping(target = "actions", constant = "1")
    ContractListDTO toListDTO(Contract contract, File signedFile);

    @Named("mapDates")
    default ContractListDTO.DatesDTO mapDates(Contract contract) {
        return new ContractListDTO.DatesDTO(
                contract.getSignatureDate(),
                contract.getStartDate(),
                contract.getEndDate(),
                mapTrialPeriod(contract)
        );
    }


    @Named("mapTrialPeriod")
    default ContractListDTO.DatesDTO.TrialPeriodDTO mapTrialPeriod(Contract contract) {
        if (contract.getTrialPeriod() == null) return null;

        List<ContractConditionId> acceptanceCriteria =
                contract.getTrialPeriod().getTrialPeriodCriteriaList() != null
                        ? contract.getTrialPeriod().getTrialPeriodCriteriaList()
                        .stream()
                        .map(tpCriteria -> tpCriteria.getTrialCriteria().getId())
                        .collect(Collectors.toList())
                        : null;

        return new ContractListDTO.DatesDTO.TrialPeriodDTO(
                contract.getTrialPeriod().getEnabled(),
                contract.getTrialPeriod().getDurationMonths(),
                contract.getTrialPeriod().getDurationDays(),
                contract.getTrialPeriod().getEndDate(),
                contract.getTrialPeriod().getRenewable(),
                contract.getTrialPeriod().getMaxRenewals(),
                contract.getTrialPeriod().getConditions(),
                acceptanceCriteria
        );
    }

    default ContractListDTO.SignedDocumentDTO mapSignedDocument(File file) {
        if (file == null) return null;

        return new ContractListDTO.SignedDocumentDTO(
                file.getStoragePath(),
                file.getFileName(),
                file.getUploadedAt()
        );
    }

    default ContractListDTO.LegalDTO mapLegalStatic() {
        return new ContractListDTO.LegalDTO(true, true, true, true, true);
    }
}
