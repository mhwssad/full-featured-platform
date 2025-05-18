package com.example.cybz_back.validation.validator;


import com.example.cybz_back.validation.annotation.Captcha;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CaptchaValidator implements ConstraintValidator<Captcha, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return false;
        }
        String regexp = "^[a-zA-Z0-9]{5}$";
        return s.matches(regexp);
    }
}
