package com.tarmiz.SIRH_backend.mapper.EmployeeMapper;

import com.tarmiz.SIRH_backend.enums.Contract.ContractStatusEnum;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeesListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeesListMapper {

    @Mapping(target = "fullName",
            expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")

    @Mapping(target = "professionalCategory",
            constant = "employes_assimiles")

    @Mapping(target = "hireDate",
            expression = "java(getHireDate(employee))")

    @Mapping(target = "contractType",
            expression = "java(getContractType(employee))")

    @Mapping(target = "status",
            expression = "java(employee.getStatus() != null ? employee.getStatus().getLabel() : null)")

    @Mapping(target = "actions",
            constant = "1")

    EmployeesListDTO toListItem(Employee employee);


    /* ================= Business Logic ================= */

    default Contract getActiveContract(Employee employee) {
        if (employee.getContracts() == null) {
            return null;
        }

        return employee.getContracts().stream()
                .filter(c -> c.getStatus() == ContractStatusEnum.ACTIF)
                .findFirst()
                .orElse(null);
    }

    default String getContractType(Employee employee) {
        Contract contract = getActiveContract(employee);
        return contract != null ? contract.getType().name() : null;
    }

    default String getHireDate(Employee employee) {
        Contract contract = getActiveContract(employee);
        return contract != null && contract.getStartDate() != null
                ? contract.getStartDate().toString()
                : null;
    }
}
