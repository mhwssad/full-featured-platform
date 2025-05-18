package com.example.cybz_back.service.mysql.auth.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.auth.Permission;
import com.example.cybz_back.mapper.PermissionMapper;
import com.example.cybz_back.service.mysql.auth.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liujian
 * @description 针对表【permission】的数据库操作Service实现
 * @createDate 2025-05-10 13:02:35
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

    @Override
    public List<Permission> getPermissionByRoleId(List<Long> permissionByRoleId) {
        return new LambdaQueryChainWrapper<>(this.baseMapper)
                .in(Permission::getPermId, permissionByRoleId)
                .list();
    }

}




