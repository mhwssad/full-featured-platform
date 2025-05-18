package com.example.cybz_back.validation.annotation;

import com.example.cybz_back.validation.validator.CaptchaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {CaptchaValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Captcha {
    String message() default "验证码必须是5位数大小写字母和数字组成";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
