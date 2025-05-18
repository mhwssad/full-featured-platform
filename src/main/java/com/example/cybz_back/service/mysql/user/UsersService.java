package com.example.cybz_back.service.mysql.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.dto.ReviseDataDto;
import com.example.cybz_back.dto.ReviseImageDao;
import com.example.cybz_back.entity.mysql.user.Users;

import java.io.IOException;

/**
 * @author liujian
 * @description 针对表【users】的数据库操作Service
 * @createDate 2025-05-10 11:26:36
 */
public interface UsersService extends IService<Users> {

    Users addUser();

    Users getByUserId(Long userId);

    Users getByCacheUserID(Long username);

    void updateByReviseData(ReviseDataDto user);

    void updateByAvatar(ReviseImageDao avatar) throws IOException;

    void updateByBackground(ReviseImageDao background);
}
