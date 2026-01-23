package com.tarmiz.SIRH_backend.exception.BusinessException;

public class EmployeeNotFoundException extends BusinessException {
    public EmployeeNotFoundException(Long id) {
        super("Employee with id " + id + " not found");
    }
}
