package com.example.cybz_back.service.mysql.auth.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.auth.RolePermission;
import com.example.cybz_back.mapper.RolePermissionMapper;
import com.example.cybz_back.service.mysql.auth.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liujian
 * @description 针对表【role_permission】的数据库操作Service实现
 * @createDate 2025-05-10 13:02:35
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements RolePermissionService {
    @Override
    public List<Long> getPermissionByRoleId(Long roleId) {
        return new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(RolePermission::getRoleId, roleId)
                .list()
                .stream().map(RolePermission::getPermId)
                .toList();
    }
}




