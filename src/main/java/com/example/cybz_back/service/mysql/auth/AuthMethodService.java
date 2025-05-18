package com.example.cybz_back.service.mysql.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.entity.mysql.auth.AuthMethod;

/**
 * @author liujian
 * @description 针对表【auth_method】的数据库操作Service
 * @createDate 2025-05-10 13:02:35
 */
public interface AuthMethodService extends IService<AuthMethod> {
    AuthMethod getUserByEmail(String email);

    AuthMethod getByAuthKey(String username);

    AuthMethod getByUserId(Long userId);

    void updateByTime(Long userId, String ip);
}
