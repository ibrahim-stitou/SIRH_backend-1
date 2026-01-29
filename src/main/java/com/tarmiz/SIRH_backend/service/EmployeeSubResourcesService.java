package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.exception.BusinessException.EmployeeNotFoundException;
import com.tarmiz.SIRH_backend.exception.BusinessException.EmployeeSubRessourcesNotFoundException;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class EmployeeSubResourcesService {

    private final EmployeeRepository employeeRepository;

    public EmployeeSubResourcesService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public <T> Employee addSubResource(Long employeeId, T subResource, Function<Employee, java.util.List<T>> getter, BiConsumer<T, Employee> setter) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        setter.accept(subResource, employee);
        getter.apply(employee).add(subResource);

        return employeeRepository.save(employee);
    }

    @Transactional
    public <E, D> Employee updateSubResource(
            Long employeeId,
            Long subId,
            D updates,
            Function<Employee, List<E>> getter,
            BiConsumer<E, D> patchFunction,
            Function<E, Long> idGetter,
            String resourceName
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        E entity = getter.apply(employee).stream()
                .filter(e -> idGetter.apply(e).equals(subId))
                .findFirst()
                .orElseThrow(() -> new EmployeeSubRessourcesNotFoundException(resourceName, subId));

        patchFunction.accept(entity, updates);

        return employeeRepository.save(employee);
    }

    @Transactional
    public <T> Employee deleteSubResource(Long employeeId, Long subId, Function<Employee, java.util.List<T>> getter, String resourceName) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        T entity = getter.apply(employee).stream()
                .filter(e -> {
                    try {
                        return (Long) e.getClass().getMethod("getId").invoke(e) == subId;
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new EmployeeSubRessourcesNotFoundException(resourceName, subId));

        getter.apply(employee).remove(entity);
        return employeeRepository.save(employee);
    }
}