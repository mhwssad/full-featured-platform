package com.example.cybz_back.dto;

import com.example.cybz_back.validation.annotation.Captcha;
import com.example.cybz_back.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


@Data
@Validated
public class LoginDto {
    @NotBlank(groups = {EnableCaptcha.class, EnablePassword.class})
    @Email(groups = {EnableCaptcha.class, EnablePassword.class})
    private String username;

    @Password(groups = {EnablePassword.class})
    private String password;

    @Captcha(groups = {EnableCaptcha.class})
    private String captcha;

    public interface EnableCaptcha {
    }

    public interface EnablePassword {
    }

}
