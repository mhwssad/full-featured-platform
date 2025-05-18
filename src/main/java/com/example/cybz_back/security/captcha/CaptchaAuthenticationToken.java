package com.example.cybz_back.security.captcha;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal; // 已认证时为UserDetails
    private final String captcha;   // 已认证时为null

    // 未认证构造函数（用户名 + 验证码）
    public CaptchaAuthenticationToken(String username, String captcha) {
        super(null);
        this.principal = username;
        this.captcha = captcha;
        setAuthenticated(false);
    }

    // 已认证构造函数（UserDetails + 权限）
    public CaptchaAuthenticationToken(UserDetails principal,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.captcha = null;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return captcha;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CaptchaAuthenticationToken that)) return false;

        if (!principal.equals(that.principal)) return false;
        return captcha == null ? that.captcha == null : captcha.equals(that.captcha);
    }


    @Override
    public int hashCode() {
        int result = principal != null ? principal.hashCode() : 0;
        result = 31 * result + (captcha != null ? captcha.hashCode() : 0);
        return result;
    }

}
