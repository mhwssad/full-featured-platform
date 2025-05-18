package com.example.cybz_back.service.cybz.user.impl;

import com.example.cybz_back.security.SecurityUser;
import com.example.cybz_back.security.captcha.CaptchaAuthenticationToken;
import com.example.cybz_back.service.cybz.user.UserService;
import com.example.cybz_back.service.mysql.auth.AuthMethodService;
import com.example.cybz_back.service.mysql.user.UserTokenService;
import com.example.cybz_back.utils.JSONResult;
import com.example.cybz_back.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserTokenService userTokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthMethodService authMethodService;


    @Transactional
    public JSONResult<?> login(String username, String password, String request) {
        // 1. 创建未认证的Token（使用原始用户名+密码）
        UsernamePasswordAuthenticationToken unauthenticatedToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return JSONResult.created(getToken(unauthenticatedToken, request));
    }

    @Transactional
    public JSONResult<?> captcha(String email, String captcha, String request) {

        // 1. 创建未认证的Token（使用原始用户名+密码）
        CaptchaAuthenticationToken unauthenticatedToken =
                new CaptchaAuthenticationToken(email, captcha);
        String token = getToken(unauthenticatedToken, request);
        return JSONResult.created(token);

    }

    private String getToken(AbstractAuthenticationToken captchaAuthenticationToken, String ip) {
        // 2. 触发认证流程（自动调用UserDetailsService和PasswordEncoder）
        Authentication authenticated = authenticationManager.authenticate(captchaAuthenticationToken);
        // 3. 生成JWT（使用认证后的主体）
        SecurityUser userDetails = (SecurityUser) authenticated.getPrincipal();
        String token = generateToken(userDetails);
        userTokenService.insertByToken(userDetails.getUserId(), token);
        authMethodService.updateByTime(userDetails.getUserId(), ip);
        return token;
    }

    private String generateToken(SecurityUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUserId());
        return JwtUtil.generateToken(claims);
    }
}
