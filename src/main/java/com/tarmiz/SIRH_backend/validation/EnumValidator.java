package com.tarmiz.SIRH_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Set<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        ignoreCase = annotation.ignoreCase();

        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {

        if (value == null) return true;

        boolean valid = ignoreCase
                ? acceptedValues.stream().anyMatch(v -> v.equalsIgnoreCase(value.name()))
                : acceptedValues.contains(value.name());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Valeur invalide. Valeurs autorisées : " + acceptedValues
            ).addConstraintViolation();
        }

        return valid;
    }
}