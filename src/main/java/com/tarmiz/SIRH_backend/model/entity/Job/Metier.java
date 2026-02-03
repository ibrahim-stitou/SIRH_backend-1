package com.tarmiz.SIRH_backend.model.entity.Job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "metier")
public class Metier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String libelle;

    private String domaine;

    @Column(nullable = false)
    private String statut = "actif";

    @OneToMany(mappedBy = "metier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emploi> emplois;

}