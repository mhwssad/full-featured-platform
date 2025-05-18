package com.example.cybz_back.security;


import com.example.cybz_back.utils.JSONResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义注销成功处理类 当用户成功注销时，该类会处理相关的请求
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class LogoutStatusSuccessHandler implements LogoutSuccessHandler {

    /**
     * 注销成功时调用的方法 该方法会验证并删除用户的token，然后返回注销成功的响应
     *
     * @param request        HTTP请求对象，用于获取请求头中的token
     * @param response       HTTP响应对象，用于向客户端发送响应
     * @param authentication 认证信息对象，包含用户认证相关的信息
     * @throws IOException      如果在处理响应时发生I/O错误
     * @throws ServletException 如果在处理请求时发生Servlet相关的错误
     */
    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        commence(response);
    }

    /**
     * 发送注销成功响应的方法 该方法会设置响应的字符编码、内容类型，并写入注销成功的JSON结果
     *
     * @param response HTTP响应对象，用于向客户端发送响应
     * @throws IOException 如果在处理响应时发生I/O错误
     */
    public void commence(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(JSONResult.created("注销成功").toJsonString());
    }
}
