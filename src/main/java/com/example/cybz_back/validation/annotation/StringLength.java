package com.example.cybz_back.validation.annotation;


import com.example.cybz_back.validation.validator.StringLengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringLengthValidator.class)
public @interface StringLength {
    String message() default "字符串长度必须在3到12之间";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
