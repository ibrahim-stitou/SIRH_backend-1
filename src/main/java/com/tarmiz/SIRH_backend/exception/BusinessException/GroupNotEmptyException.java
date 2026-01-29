package com.tarmiz.SIRH_backend.exception.BusinessException;

public class GroupNotEmptyException extends BusinessException {
    public GroupNotEmptyException(Long groupId) {
        super("Impossible de supprimer le groupe non vide (ID=" + groupId + ")");
    }
}