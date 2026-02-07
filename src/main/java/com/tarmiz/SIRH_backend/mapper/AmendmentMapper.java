//package com.tarmiz.SIRH_backend.mapper;
//
//import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantDetailDTO;
//import com.tarmiz.SIRH_backend.model.DTO.ContractDTOs.AvenantListDTO;
//import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface AmendmentMapper {
//
//    @Mapping(target = "id", expression = "java(String.valueOf(amendment.getId()))")
//    @Mapping(target = "contractReference", source = "contract.reference")
//    @Mapping(target = "typeModification", expression = "java(amendment.getTypeModification().name())")
//    @Mapping(target = "status", expression = "java(amendment.getStatus().name())")
//    @Mapping(target = "documents", ignore = true)
//    @Mapping(target = "createdAt", expression = "java(amendment.getCreatedDate() != null ? amendment.getCreatedDate().toString() : null)")
//    AvenantListDTO toListDTO(Amendment amendment);
//
//    List<AvenantListDTO> toListDTO(List<Amendment> amendments);
//
//    @Mapping(target = "id", expression = "java(amendment.getId().toString())")
//    @Mapping(target = "contractReference", source = "amendment.contract.reference")
//    @Mapping(target = "date", expression = "java(amendment.getAmendmentDate().toString())")
//    @Mapping(target = "typeModification", expression = "java(amendment.getTypeModification().name())")
//    @Mapping(target = "status", expression = "java(amendment.getStatus().name())")
//    @Mapping(target = "createdAt", expression = "java(amendment.getCreatedDate().toString())")
//    @Mapping(target = "createdBy", source = "amendment.createdBy")
//    @Mapping(target = "actions", expression = "java(java.util.Collections.singletonList(\"Modifier\"))")
//    @Mapping(target = "contractId", expression = "java(amendment.getContract().getId().toString())")
//    @Mapping(target = "motif", source = "amendment.motif")
//    @Mapping(target = "description", source = "amendment.description")
//    @Mapping(target = "notes", source = "amendment.notes")
//    @Mapping(target = "validations.manager", expression = "java(false)")
//    @Mapping(target = "validations.rh", expression = "java(false)")
//    @Mapping(target = "documentUrl", expression = "java(null)")
//    @Mapping(target = "signedDocument", expression = "java(null)")
//    @Mapping(target = "changes.salary.apres.salaryBrut", source = "amendment.contract.salary.salaryBrut")
//    @Mapping(target = "changes.salary.apres.salaryNet", source = "amendment.contract.salary.salaryNet")
//    @Mapping(target = "changes.salary.apres.currency", source = "amendment.contract.salary.currency")
//    @Mapping(target = "changes.salary.apres.paymentMethod", expression = "java(amendment.contract.salary.getPaymentMethod().name())")
//    @Mapping(target = "changes.salary.avant", expression = "java(null)")
//    @Mapping(target = "changes.schedule.apres.scheduleType", expression = "java(amendment.contract.schedule.getScheduleType().name())")
//    @Mapping(target = "changes.schedule.apres.shiftWork", source = "amendment.contract.schedule.shiftWork")
//    @Mapping(target = "changes.schedule.apres.annualLeaveDays", source = "amendment.contract.schedule.annualLeaveDays")
//    @Mapping(target = "changes.schedule.apres.otherLeaves", source = "amendment.contract.schedule.otherLeaves")
//    @Mapping(target = "changes.schedule.avant", expression = "java(null)")
//    @Mapping(target = "changes.job.apres.posteId", expression = "java(amendment.contract.job.getPoste().getId().toString())")
//    @Mapping(target = "changes.job.apres.departementId", expression = "java(amendment.contract.job.getPoste().getDepartement().getId().toString())")
//    @Mapping(target = "changes.job.apres.workMode", expression = "java(amendment.contract.job.getWorkMode().name())")
//    @Mapping(target = "changes.job.apres.classification", source = "amendment.contract.job.classification")
//    @Mapping(target = "changes.job.apres.level", expression = "java(amendment.contract.job.getLevel().name())")
//    @Mapping(target = "changes.job.apres.responsibilities", source = "amendment.contract.job.responsibilities")
//    @Mapping(target = "changes.job.avant", expression = "java(null)")
//    AvenantDetailDTO toDetailDTO(Amendment amendment);
//}
