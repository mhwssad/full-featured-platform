package com.example.cybz_back.validation.validator;

import com.example.cybz_back.validation.annotation.StringLength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringLengthValidator implements ConstraintValidator<StringLength, String> {

    @Override
    public void initialize(StringLength constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 如果值为null，不进行验证，可以结合@NotNull注解使用
        }
        int length = value.length();
        return length >= 3 && length <= 12;
    }
}