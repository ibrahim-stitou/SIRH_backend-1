package com.tarmiz.SIRH_backend.enums.Contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Statut du contrat",
        example = "Brouillon",
        allowableValues = {
                "Brouillon",
                "En_attente_signature",
                "Actif",
                "Periode_essai",
                "Suspendu",
                "En_preavis",
                "Resilie",
                "Expire",
                "Renouvele",
                "Archive"
        }
)
public enum ContractStatusEnum { BROUILLON, EN_ATTENTE_SIGNATURE, ACTIF, PERIODE_ESSAI, SUSPENDU, EN_PREAVIS, RESILIE, EXPIRE, RENOUVELE, ARCHIVE }

