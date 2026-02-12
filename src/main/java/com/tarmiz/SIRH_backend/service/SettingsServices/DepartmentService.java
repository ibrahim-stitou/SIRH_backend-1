package com.tarmiz.SIRH_backend.service.SettingsServices;

import com.tarmiz.SIRH_backend.enums.DepartmentStatus;
import com.tarmiz.SIRH_backend.exception.BusinessException.BusinessException;
import com.tarmiz.SIRH_backend.mapper.CompanyHierarchyMappers.DepartmentMapper;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.DepartmentListDTO;
import com.tarmiz.SIRH_backend.model.DTO.DepartmentCreationDTO;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Company;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.CompanyRepository;
import com.tarmiz.SIRH_backend.model.repository.CompanyHierarchyRepos.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final CompanyRepository companyRepository;

    public List<DepartmentListDTO> listAll() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toListDto)
                .toList();
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Department not found"));
    }

    public DepartmentCreationDTO create(DepartmentCreationDTO departmentCreationDTO) {
        Company company = companyRepository.findById(1L)
                .orElseThrow(() -> new BusinessException(
                        "Company not found with id: " + departmentCreationDTO.getCompanyId()
                ));

        Department department = new Department();
        department.setName(departmentCreationDTO.getName());
        department.setNameAr(departmentCreationDTO.getNameAr());
        department.setDescription(departmentCreationDTO.getDescription());
        department.setCompany(company);
        department.setStatus(departmentCreationDTO.getStatus() != null ? departmentCreationDTO.getStatus() : DepartmentStatus.ACTIVE);
        Department savedDepartment = departmentRepository.save(department);
        DepartmentCreationDTO result = new DepartmentCreationDTO();
        result.setId(savedDepartment.getId());
        result.setName(savedDepartment.getName());
        result.setNameAr(savedDepartment.getNameAr());
        result.setDescription(savedDepartment.getDescription());
        result.setStatus(savedDepartment.getStatus());
        result.setCompanyId(savedDepartment.getCompany().getId());
        return result;
    }

    public Department update(Long id, Department updated) {
        Department dept = getById(id);
        dept.setName(updated.getName());
        dept.setNameAr(updated.getNameAr());
        dept.setDescription(updated.getDescription());
        return departmentRepository.save(dept);
    }

    public void delete(Long id) {
        Department dept = getById(id);

        if (!dept.getEmployees().isEmpty()) {
            throw new BusinessException("Cannot delete department with assigned employees");
        }
        if (dept.getStatus() != DepartmentStatus.INACTIVE) {
            throw new BusinessException("Department must be inactive to delete");
        }

        departmentRepository.delete(dept);
    }

    public DepartmentListDTO activate(Long id) {
        Department dept = getById(id);
        dept.setStatus(DepartmentStatus.ACTIVE);
        Department saved = departmentRepository.save(dept);
        return departmentMapper.toListDto(saved);
    }

    public DepartmentListDTO deactivate(Long id) {
        Department dept = getById(id);
        dept.setStatus(DepartmentStatus.INACTIVE);
        Department saved = departmentRepository.save(dept);
        return departmentMapper.toListDto(saved);
    }
}