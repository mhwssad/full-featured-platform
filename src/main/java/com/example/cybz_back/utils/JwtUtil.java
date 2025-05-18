package com.example.cybz_back.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2
public class JwtUtil {
    // JWT 密钥（需要从配置文件中加载）
    private static final String secret = "rI7kK0pK0hD8dK9oQ6kR0bK3dF1mJ7dT2yM3wT7yF9fT5mX1aX4aH0gB6rL1vZ4gI1jG3qC8";
    // 默认过期时间 7 天（单位：分钟）
    private static final long expirationMinutes = 10080;
    // 签发者标识
    private static final String issuer = "douyin-app";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private static final long expirationMillis = TimeUnit.MINUTES.toMillis(expirationMinutes);

    private JwtUtil() {
    }


    /**
     * 生成通用 Token（包含自定义 claims）
     */
    public static String generateToken(Map<String, Object> claims) {
        return buildToken(claims, expirationMillis);
    }

    /**
     * 生成用户 Token（专为身份验证设计）
     */
    public static String generateUserToken(String userId) {
        return Jwts.builder()
                .issuer(issuer)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析并验证 Token
     */
    public static Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 高级验证（带详细错误类型判断）
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return isTokenNotExpired(claims);
        } catch (ExpiredJwtException ex) {
            log.warn("Token expired: {}", ex.getMessage());
            return false;
        } catch (SecurityException ex) {
            log.error("Invalid signature: {}", ex.getMessage());
            return false;
        } catch (MalformedJwtException ex) {
            log.error("Malformed token: {}", ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("Empty token");
            return false;
        } catch (JwtException ex) {
            log.error("JWT error: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * 刷新 Token 过期时间
     */
    public static String refreshToken(String token) throws JwtException {
        Claims claims = parseToken(token);
        return buildToken(claims, expirationMillis);
    }

    /**
     * 构建JWT令牌
     *
     * @param claims     令牌中包含的自定义声明和信息
     * @param expiration 令牌的过期时间，单位为毫秒
     * @return 生成的JWT令牌字符串
     */
    private static String buildToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public static boolean isTokenNotExpired(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    /**
     * 获取 Token 的剩余有效时间（单位：秒）
     *
     * @param token JWT 字符串
     * @return 剩余有效时间（单位：秒）。如果已过期，则返回负值。
     */
    public static long getRemainingTimeInSeconds(String token) {
        try {
            Claims claims = parseToken(token);
            Date expirationDate = claims.getExpiration();
            long remainingMillis = expirationDate.getTime() - System.currentTimeMillis();
            return TimeUnit.MILLISECONDS.toSeconds(remainingMillis); // 转换为秒
        } catch (ExpiredJwtException ex) {
            log.warn("Token expired: {}", ex.getMessage());
            return -1; // 已过期，返回 -1 表示无效
        } catch (JwtException ex) {
            log.error("JWT error while getting remaining time: {}", ex.getMessage());
            throw new RuntimeException("无法获取 Token 剩余时间", ex);
        }
    }
}
