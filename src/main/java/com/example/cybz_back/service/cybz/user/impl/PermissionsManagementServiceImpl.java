package com.example.cybz_back.service.cybz.user.impl;

import com.example.cybz_back.entity.mysql.auth.Permission;
import com.example.cybz_back.entity.mysql.auth.Role;
import com.example.cybz_back.entity.mysql.auth.UserRole;
import com.example.cybz_back.service.cybz.user.PermissionsManagementService;
import com.example.cybz_back.service.mysql.auth.PermissionService;
import com.example.cybz_back.service.mysql.auth.RolePermissionService;
import com.example.cybz_back.service.mysql.auth.RoleService;
import com.example.cybz_back.service.mysql.auth.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionsManagementServiceImpl implements PermissionsManagementService {

    private final RoleService roleService;

    private final PermissionService permissionService;


    private final UserRoleService userRoleService;


    private final RolePermissionService rolePermissionService;

    @Override
    public Collection<? extends GrantedAuthority> getPermission(Long userId) {
        UserRole roleIdByUserId = userRoleService.getRoleIdByUserId(userId);
        List<Long> permissionByRoleId = rolePermissionService.getPermissionByRoleId(roleIdByUserId.getRoleId());
        List<Permission> permissionByRoleId1 = permissionService.getPermissionByRoleId(permissionByRoleId);
        return permissionByRoleId1.stream()
                .map(Permission::getPermCode)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }


    @Override
    public void setPermission(Long userId) {
        Role rolePermissions = roleService.getRolePermissions();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(rolePermissions.getRoleId());
        userRoleService.save(userRole);
    }
}
