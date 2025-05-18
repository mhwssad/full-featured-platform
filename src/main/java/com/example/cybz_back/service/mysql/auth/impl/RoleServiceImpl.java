package com.example.cybz_back.service.mysql.auth.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.auth.Role;
import com.example.cybz_back.mapper.RoleMapper;
import com.example.cybz_back.service.mysql.auth.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author liujian
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2025-05-10 13:02:35
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Override
    public Role getRolePermissions() {
        return new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(Role::getIsEnabled, 1)
                .eq(Role::getIsDefault, 1)
                .one();
    }
}




