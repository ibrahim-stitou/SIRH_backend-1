package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.enums.Contract.AmendmentStatus;
import com.tarmiz.SIRH_backend.enums.Contract.AmendmentType;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "contract_amendments")
@Getter
@Setter
public class Amendment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 50)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "amendment_date", nullable = false)
    private LocalDate amendmentDate;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "objet", nullable = false, length = 255)
    private String objet;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AmendmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_modification", nullable = false, length = 50)
    private AmendmentType typeModification;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}