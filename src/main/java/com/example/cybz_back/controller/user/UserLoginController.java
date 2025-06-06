package com.example.cybz_back.controller.user;

import com.example.cybz_back.dto.LoginDto;
import com.example.cybz_back.service.cybz.user.UserService;
import com.example.cybz_back.utils.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "登录接口")
public class UserLoginController {
    private final UserService userService;

    @Operation(summary = "验证码登录接口")
    @Parameters(
            value = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "username",
                            description = "用户名",
                            required = true
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "captcha",
                            description = "验证码",
                            required = true
                    )
            }
    )
    @PostMapping("/captcha")
    public JSONResult<?> captcha(@RequestBody @Validated(LoginDto.EnableCaptcha.class) LoginDto loginDto,
                                 HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return userService.captcha(loginDto.getUsername(), loginDto.getCaptcha(), ipAddress);
    }

    @PostMapping("/login")
    public JSONResult<?> login(@RequestBody @Validated(LoginDto.EnablePassword.class) LoginDto loginDto,
                               HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return userService.login(loginDto.getUsername(), loginDto.getPassword(), ipAddress);
    }

}
