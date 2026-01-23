package com.tarmiz.SIRH_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

    Class<? extends Enum<?>> enumClass();

    boolean ignoreCase() default true;

    String message() default "Valeur invalide. Valeurs autorisées : {enumValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}