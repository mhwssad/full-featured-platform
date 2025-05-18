package com.example.cybz_back.controller.mail;

import com.example.cybz_back.dto.EmailMessage;
import com.example.cybz_back.service.cybz.mail.MailRedisService;
import com.example.cybz_back.utils.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
@Validated
@Tag(name = "发送邮件接口")
public class CaptchaMailController {
    private final MailRedisService mailRedisService;

    @Operation(summary = "发送验证码邮件")
    @PostMapping("/mail")
    public JSONResult<Void> sendCaptchaMail(@RequestBody @Valid EmailMessage email) {
        mailRedisService.sendCaptchaMail(email.getEmail());
        return JSONResult.ok();
    }
}
