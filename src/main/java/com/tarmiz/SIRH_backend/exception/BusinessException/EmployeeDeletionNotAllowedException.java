package com.tarmiz.SIRH_backend.exception.BusinessException;

public class EmployeeDeletionNotAllowedException extends BusinessException {

    public EmployeeDeletionNotAllowedException(Long id) {
        super("Employee with id " + id + " cannot be deleted due to existing constraints");
    }
}