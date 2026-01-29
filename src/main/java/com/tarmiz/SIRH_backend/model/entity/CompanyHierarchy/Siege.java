package com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy;

import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sieges",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        })
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    private String phone;

    @Email
    private String email;

    @OneToMany(mappedBy = "siege", cascade = CascadeType.ALL)
    private List<Group> groups;
}