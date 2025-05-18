package com.example.cybz_back.security;


import com.example.cybz_back.entity.mysql.auth.AuthMethod;
import com.example.cybz_back.entity.mysql.user.Users;
import com.example.cybz_back.service.cybz.user.PermissionsManagementService;
import com.example.cybz_back.service.mysql.auth.AuthMethodService;
import com.example.cybz_back.service.mysql.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MultiAuthUserDetailsService {
    private final UsernameUserDetailsService usernameUserDetailsService;
    private final AuthMethodService authMethodService;
    private final UsersService usersService;
    private final PermissionsManagementService permissionsManagementService;

    public UserDetails loadUserByCaptcha(String username) {
        registeredUser(username);
        return usernameUserDetailsService.loadUserByUsername(username);
    }


    public UserDetails loadUserByToken(Long userId) {
        AuthMethod authMethod = authMethodService.getByUserId(userId);
        return getUserDetails(authMethod, usersService, permissionsManagementService);
    }

    @NotNull
    static UserDetails getUserDetails(AuthMethod authMethod, UsersService usersService, PermissionsManagementService permissionsManagementService) {
        Users byUserId = usersService.getByUserId(authMethod.getUserId());
        Collection<? extends GrantedAuthority> permission = permissionsManagementService.getPermission(byUserId.getUserId());
        return new SecurityUser(
                byUserId.getUserId(),
                authMethod.getAuthKey(),
                authMethod.getAuthSecret(),
                permission,
                byUserId.getAccountNonExpired().equals(1),
                byUserId.getAccountNonLocked().equals(1),
                byUserId.getCredentialsNonExpired().equals(1),
                byUserId.getEnabled().equals(1)
        );
    }

    public void registeredUser(String email) {
        AuthMethod byAuthKey = authMethodService.getByAuthKey(email);
        if (byAuthKey != null) {
            return;
        }
        Users users = usersService.addUser();
        AuthMethod authMethod = assembleAuthMethod(users.getUserId(), email);
        if (!authMethodService.save(authMethod)) {
            throw new IllegalStateException("无法完成注册：认证方法保存失败");
        }
        permissionsManagementService.setPermission(users.getUserId());
    }


    private AuthMethod assembleAuthMethod(Long userId, String authKey) {
        AuthMethod authMethod = new AuthMethod();
        authMethod.setUserId(userId);
        authMethod.setAuthKey(authKey);
        authMethod.setAuthType("email");
        return authMethod;
    }
}
