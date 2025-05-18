package com.example.cybz_back.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "发送邮件")
public class EmailMessage {
    @Schema(description = "邮箱地址")
    @Email
    @NotBlank
    private String email;
    @Schema(description = "验证码")
    private String captcha;
}
