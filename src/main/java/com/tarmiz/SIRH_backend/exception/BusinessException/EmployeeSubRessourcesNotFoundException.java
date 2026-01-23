package com.tarmiz.SIRH_backend.exception.BusinessException;

public class EmployeeSubRessourcesNotFoundException extends BusinessException {
    public EmployeeSubRessourcesNotFoundException(String resource, Long id) {
        super("Employee sub resources not found"+ resource + " with id " + id + " not found");
    }
}
