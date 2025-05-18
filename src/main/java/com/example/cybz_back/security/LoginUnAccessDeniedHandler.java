package com.example.cybz_back.security;

import com.example.cybz_back.utils.JSONResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义无权限访问处理器 当用户认证通过但没有足够权限访问特定资源时，此处理器会处理403错误
 */
@Component
public class LoginUnAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // 设置响应编码和类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        // 发送认证失败的响应信息
        response.getWriter().write(JSONResult.notFound("权限不足").toJsonString());
    }
}
