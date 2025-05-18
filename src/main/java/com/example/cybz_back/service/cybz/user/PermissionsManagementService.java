package com.example.cybz_back.service.cybz.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface PermissionsManagementService {
    Collection<? extends GrantedAuthority> getPermission(Long userId);

    void setPermission(Long userId);
}
