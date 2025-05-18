package com.example.cybz_back.security;

import com.example.cybz_back.utils.JSONResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义登录认证失败处理类 当用户尝试访问受保护的资源但未进行身份验证时，此类会处理响应
 */
@Component
@Log4j2
public class LoginUnAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    /**
     * 处理认证失败的请求
     *
     * @param request       HTTP请求对象，包含请求信息
     * @param response      HTTP响应对象，用于向客户端发送响应
     * @param authException 认证异常对象，包含认证失败的原因
     * @throws IOException      如果在处理响应期间发生I/O错误
     * @throws ServletException 如果在处理响应期间发生Servlet异常
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        log.warn("认证失败: {}, 访问路径: {}", authException.getMessage(), request.getRequestURI()); // 添加日志记录
        // 设置响应编码和类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        // 发送认证失败的响应信息
        response.getWriter().write(JSONResult.notFound(authException.getMessage()).toJsonString());
    }
}