package com.example.cybz_back.security.captcha;

import com.example.cybz_back.security.MultiAuthUserDetailsService;
import com.example.cybz_back.service.redis.CaptchaRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaptchaAuthenticationProvider implements AuthenticationProvider {

    private final CaptchaRedisService captchaRedisService;

    private final MultiAuthUserDetailsService multiAuthUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 获取用户名和验证码
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new BadCredentialsException("无效的认证请求");
        }
        String captcha = (String) authentication.getCredentials();

        // 2. 校验验证码
        if (!captchaRedisService.checkCaptcha(username, captcha)) {
            throw new BadCredentialsException("登录失败，请检查验证码");
        }

        // 3. 加载用户信息（处理用户不存在情况）
        UserDetails userDetails;
        try {
            userDetails = multiAuthUserDetailsService.loadUserByCaptcha(username);
        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("登录失败，请检查用户名");
        }

        // 4. 构造已认证的Token（包含权限）
        CaptchaAuthenticationToken authenticatedToken = new CaptchaAuthenticationToken(
                userDetails,
                userDetails.getAuthorities()
        );
        authenticatedToken.setDetails(authentication.getDetails());
        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
