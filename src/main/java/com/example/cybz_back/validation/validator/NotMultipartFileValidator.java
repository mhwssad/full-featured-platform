package com.example.cybz_back.validation.validator;


import com.example.cybz_back.validation.annotation.NotMultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class NotMultipartFileValidator implements ConstraintValidator<NotMultipartFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        // 检查文件是否为空
        if (file == null) {
            // 文件为空时，设置约束验证上下文的错误信息
            constraintValidatorContext.buildConstraintViolationWithTemplate("{file.null}")
                    .addConstraintViolation();
            return false;
        }

        // 检查文件是否为空文件
        if (file.isEmpty()) {
            // 文件为空时，设置约束验证上下文的错误信息
            constraintValidatorContext.buildConstraintViolationWithTemplate("{file.empty}")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
