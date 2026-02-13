package com.tarmiz.SIRH_backend.mapper.EmployeeMapper;
import com.tarmiz.SIRH_backend.model.DTO.EmployeeDTOs.EmployeeSimpleDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeSimpleMapper {

    EmployeeSimpleMapper INSTANCE = Mappers.getMapper(EmployeeSimpleMapper.class);

    EmployeeSimpleDTO toDTO(Employee employee);

    List<EmployeeSimpleDTO> toDTOList(List<Employee> employees);
}