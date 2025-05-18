package com.example.cybz_back.validation.validator;

import com.example.cybz_back.validation.annotation.Privacy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrivacyValidator implements ConstraintValidator<Privacy, String> {
    private boolean allowEmpty;

    @Override
    public void initialize(Privacy constraintAnnotation) {
        // 获取注解中的 allowEmpty 属性
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (allowEmpty && (s == null || s.isEmpty())) {
            return true;
        }
        // 如果输入不为空，检查是否符合要求
        return s != null && (s.equals("public") || s.equals("private") || s.equals("friends"));
    }
}
