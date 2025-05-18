package com.example.cybz_back.service.mysql.auth.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.auth.AuthMethod;
import com.example.cybz_back.mapper.AuthMethodMapper;
import com.example.cybz_back.service.mysql.auth.AuthMethodService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author liujian
 * @description 针对表【auth_method】的数据库操作Service实现
 * @createDate 2025-05-10 13:02:35
 */
@Service
public class AuthMethodServiceImpl extends ServiceImpl<AuthMethodMapper, AuthMethod>
        implements AuthMethodService {

    @Override
    public AuthMethod getUserByEmail(String email) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(AuthMethod::getAuthType, "email")
                .eq(AuthMethod::getAuthKey, email)
                .eq(AuthMethod::getIsActive, 1)
                .one();
    }

    @Override
    public AuthMethod getByAuthKey(String username) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(AuthMethod::getAuthKey, username)
                .one();
    }

    @Override
    public AuthMethod getByUserId(Long userId) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(AuthMethod::getUserId, userId)
                .one();
    }

    @Override
    public void updateByTime(Long userId, String ip) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(AuthMethod::getUserId, userId)
                .set(AuthMethod::getUpdatedTime, LocalDateTime.now())
                .set(AuthMethod::getLastUsedIp, ip);
    }
}




