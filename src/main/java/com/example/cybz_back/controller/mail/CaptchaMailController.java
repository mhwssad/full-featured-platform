package com.example.cybz_back.controller.mail;

import com.example.cybz_back.dto.EmailMessage;
import com.example.cybz_back.service.cybz.mail.MailRedisService;
import com.example.cybz_back.utils.JSONResult;
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
public class CaptchaMailController {
    private final MailRedisService mailRedisService;

    @PostMapping("/mail")
    public JSONResult<Void> sendCaptchaMail(@RequestBody @Valid EmailMessage email) {
        mailRedisService.sendCaptchaMail(email.getEmail());
        return JSONResult.ok();
    }
}
