package com.example.cybz_back.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableAsync
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // 禁用CSRF
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(
                                "/doc.html",                   // Knife4j主页
                                "/swagger-ui.html",            // 旧版Swagger UI
                                "/swagger-ui/**",              // 新版Swagger UI静态资源
                                "/v3/api-docs/**",             // OpenAPI 3.0+ 的API元数据
                                "/webjars/**",                 // WebJars资源（如Swagger UI的JS/CSS）
                                "/swagger-resources/**",        // Swagger资源配置（兼容旧版）

                                "/send/mail",  // 发送邮件请求
                                "/user/captcha",  // 验证码登录
                                "/user/login"  // 密码登录
                        )
                        .permitAll().anyRequest().authenticated());  // 允许所有请求通过
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt算法对密码进行编码
        return new BCryptPasswordEncoder();
    }
}
