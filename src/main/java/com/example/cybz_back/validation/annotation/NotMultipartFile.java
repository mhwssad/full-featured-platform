package com.example.cybz_back.validation.annotation;

import com.example.cybz_back.validation.validator.NotMultipartFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NotMultipartFileValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface NotMultipartFile {
    String message() default "文件不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
