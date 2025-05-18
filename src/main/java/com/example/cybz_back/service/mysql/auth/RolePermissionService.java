package com.example.cybz_back.service.mysql.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.entity.mysql.auth.RolePermission;

import java.util.List;

/**
 * @author liujian
 * @description 针对表【role_permission】的数据库操作Service
 * @createDate 2025-05-10 13:02:35
 */
public interface RolePermissionService extends IService<RolePermission> {
    List<Long> getPermissionByRoleId(Long roleId);
}
