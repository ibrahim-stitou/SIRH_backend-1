package com.tarmiz.SIRH_backend.model.DTO;
import com.tarmiz.SIRH_backend.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreationDTO {
    private Long id;
    private String name;
    private String nameAr;
    private String description;
    private Long companyId;
    private DepartmentStatus status;
}