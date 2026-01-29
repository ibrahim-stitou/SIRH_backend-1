package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Type de contrat",
        example = "CDI",
        allowableValues = {
                "CDI",            // Contrat à Durée Indéterminée
                "CDD",            // Contrat à Durée Déterminée
                "CDD_Saisonnier", // CDD pour travaux saisonniers
                "CDD_Temporaire", // CDD pour travaux temporaires
                "ANAPEC",         // Contrat ANAPEC (Idmaj)
                "SIVP",           // Stage d'Insertion à la Vie Professionnelle
                "TAHIL",          // Programme TAHIL
                "Apprentissage",  // Contrat d'apprentissage
                "Stage_PFE",      // Stage de fin d'études
                "Stage_Initiation", // Stage d'initiation
                "Interim",        // Travail intérimaire
                "Teletravail",    // Contrat de télétravail (loi 2022)
                "Freelance",      // Travail indépendant
                "Consultance"     // Contrat de consultance
        }
)
public enum ContractTypeEnum { CDI, CDD, CDD_SAISONNIER, CDD_TEMPORAIRE, ANAPEC, SIVP, TAHIL, APPRENTISSAGE, STAGE_PFE, STAGE_INITIATION, INTERIM, TELETRAVAIL, FREELANCE, CONSULTANCE }