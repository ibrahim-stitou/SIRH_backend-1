package com.tarmiz.SIRH_backend.exception.BusinessException;

public class DepartmentNotFoundException extends BusinessException {

    public DepartmentNotFoundException(Long id) {
        super("Department with id " + id + " not found");
    }

    public DepartmentNotFoundException(String name) {
        super("Department with name '" + name + "' not found");
    }
}