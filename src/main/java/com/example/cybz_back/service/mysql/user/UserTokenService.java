package com.example.cybz_back.service.mysql.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.entity.mysql.user.UserToken;

/**
 * @author liujian
 * @description 针对表【user_token】的数据库操作Service
 * @createDate 2025-05-10 11:26:36
 */
public interface UserTokenService extends IService<UserToken> {

    UserToken getUserId(String token);

    void insertByToken(Long userId, String token);
}
