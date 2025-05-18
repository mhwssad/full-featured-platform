package com.example.cybz_back.security;


import com.example.cybz_back.entity.mysql.user.UserToken;
import com.example.cybz_back.service.mysql.user.UserTokenService;
import com.example.cybz_back.service.redis.TokenRedisService;
import com.example.cybz_back.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final MultiAuthUserDetailsService multiAuthUserDetailsService;
    private final TokenRedisService tokenRedisService;
    private final UserTokenService userTokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            try {
                // 前置条件：优先验证JWT自身有效性
                if (!JwtUtil.validateToken(token)) {
                    log.debug("Invalid JWT structure or signature: {}", token);
                    SecurityContextHolder.clearContext();
                    filterChain.doFilter(request, response);
                    return;
                }
                Long userId = tokenRedisService.getUserId(token);
                if (userId == null) {
                    UserToken userToken = userTokenService.getUserId(token);
                    if (userToken != null && userToken.getToken().equals(token)) {
                        tokenRedisService.saveToken(token, userToken.getUserId());
                        userId = userToken.getUserId();
                    }
                }
                if (userId != null) {
                    UserDetails userDetails = multiAuthUserDetailsService.loadUserByToken(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                log.error("token error: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
