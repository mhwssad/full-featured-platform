package com.example.cybz_back.service.mysql.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.user.UserToken;
import com.example.cybz_back.mapper.UserTokenMapper;
import com.example.cybz_back.service.mysql.user.UserTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author liujian
 * @description 针对表【user_token】的数据库操作Service实现
 * @createDate 2025-05-10 11:26:36
 */
@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken>
        implements UserTokenService {

    @Override
    public UserToken getUserId(String token) {
        return new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(UserToken::getToken, token)
                .eq(UserToken::getActiveFlag, 1)
                .eq(UserToken::getStatus, 1)
                .one();
    }

    @Override
    public void insertByToken(Long userId, String token) {
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setToken(token);
        userToken.setCreatedTime(LocalDate.now());
        userToken.setExpiredTime(LocalDate.now().plusDays(7));
        // 假设UserToken有一个id字段作为主键，这里需要查询是否存在该用户的token记录
        UserToken existingToken = getOne(new QueryWrapper<UserToken>().eq("user_id", userId));
        if (existingToken != null) {
            userToken.setTokenId(existingToken.getTokenId()); // 设置主键以确保更新
        }
        saveOrUpdate(userToken);
    }
}




