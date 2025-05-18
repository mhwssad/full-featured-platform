package com.example.cybz_back.validation.annotation;

import com.example.cybz_back.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface Password {
    String message() default "密码必须是大于8位的大小写字母，数字和特殊符号";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
