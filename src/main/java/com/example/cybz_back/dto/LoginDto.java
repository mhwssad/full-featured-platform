package com.example.cybz_back.dto;

import com.example.cybz_back.validation.annotation.Captcha;
import com.example.cybz_back.validation.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Schema(description = "登录接口参数")
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Schema(description = "用户名")
    @NotBlank(groups = {EnableCaptcha.class, EnablePassword.class})
    @Email(groups = {EnableCaptcha.class, EnablePassword.class})
    private String username;

    @Schema(description = "密码")
    @Password(groups = {EnablePassword.class})
    private String password;

    @Schema(description = "验证码")
    @Captcha(groups = {EnableCaptcha.class})
    private String captcha;

    public interface EnableCaptcha {
    }

    public interface EnablePassword {
    }

}
