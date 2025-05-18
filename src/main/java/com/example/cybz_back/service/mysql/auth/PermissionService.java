package com.example.cybz_back.service.mysql.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.entity.mysql.auth.Permission;

import java.util.List;

/**
 * @author liujian
 * @description 针对表【permission】的数据库操作Service
 * @createDate 2025-05-10 13:02:35
 */
public interface PermissionService extends IService<Permission> {
    List<Permission> getPermissionByRoleId(List<Long> permissionByRoleId);
}
