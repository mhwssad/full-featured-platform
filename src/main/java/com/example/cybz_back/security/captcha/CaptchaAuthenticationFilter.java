package com.example.cybz_back.security.captcha;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/auth/captcha", "POST");

    public CaptchaAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            // 解析JSON请求体
            Map<String, String> authRequest = new ObjectMapper().readValue(
                    request.getInputStream(),
                    new TypeReference<>() {
                    }
            );

            String username = authRequest.get("username");
            String captcha = authRequest.get("captcha");

            // 创建自定义Token
            CaptchaAuthenticationToken authToken =
                    new CaptchaAuthenticationToken(username, captcha);

            // 添加请求详情
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            return this.getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("请求格式错误", e);
        }
    }

}
