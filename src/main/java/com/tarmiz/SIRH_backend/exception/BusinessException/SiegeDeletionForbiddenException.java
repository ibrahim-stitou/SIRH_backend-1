package com.tarmiz.SIRH_backend.exception.BusinessException;

public class SiegeDeletionForbiddenException extends BusinessException {
    public SiegeDeletionForbiddenException(Long siegeId) {
        super("Impossible de supprimer le siège : des groupes sont encore rattachés (ID=" + siegeId + ")");
    }
}
