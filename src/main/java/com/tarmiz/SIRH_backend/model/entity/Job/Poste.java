package com.tarmiz.SIRH_backend.model.entity.Job;

import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.model.entity.Contract.ContractJob;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "poste",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"emploi_id", "code"})})
public class Poste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emploi_id", nullable = false)
    private Emploi emploi;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String statut = "actif";

    @OneToMany(mappedBy = "poste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractJob> contractJobs;

}

