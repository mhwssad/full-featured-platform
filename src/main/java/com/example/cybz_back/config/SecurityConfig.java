package com.example.cybz_back.config;


import com.example.cybz_back.security.*;
import com.example.cybz_back.security.captcha.CaptchaAuthenticationFilter;
import com.example.cybz_back.security.captcha.CaptchaAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableAsync
public class SecurityConfig {

    private final UsernameUserDetailsService usernameUserDetailsService;
    private final CaptchaAuthenticationProvider captchaAuthenticationProvider;
    private final LoginUnAuthenticationEntryPointHandler loginUnAuthenticationEntryPointHandler;
    private final LogoutStatusSuccessHandler logoutStatusSuccessHandler;
    private final LoginUnAccessDeniedHandler loginUnAccessDeniedHandler;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

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
        http.formLogin(AbstractHttpConfigurer::disable);
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint(loginUnAuthenticationEntryPointHandler))
                .logout(logout -> logout.logoutSuccessHandler(logoutStatusSuccessHandler))
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(loginUnAccessDeniedHandler));
        return http.build();
    }

    @Bean
    public CaptchaAuthenticationFilter captchaAuthenticationFilter() throws Exception {
        CaptchaAuthenticationFilter filter = new CaptchaAuthenticationFilter();
        filter.setAuthenticationManager(authManager(null));
        return filter;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .authenticationProvider(captchaAuthenticationProvider)  // 验证码认证提供者
                .userDetailsService(usernameUserDetailsService)         // 原有用户服务
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt算法对密码进行编码
        return new BCryptPasswordEncoder();
    }
}
