package com.tarmiz.SIRH_backend.model.entity.Job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "emploi",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"metier_id", "code"})})
public class Emploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "metier_id", nullable = false)
    private Metier metier;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String statut = "actif";

    @OneToMany(mappedBy = "emploi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poste> postes;

}
