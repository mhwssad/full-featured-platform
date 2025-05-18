package com.example.cybz_back.service.mysql.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.user.UserStat;
import com.example.cybz_back.mapper.UserStatMapper;
import com.example.cybz_back.service.mysql.user.UserStatService;
import org.springframework.stereotype.Service;

/**
 * @author liujian
 * @description 针对表【user_stat】的数据库操作Service实现
 * @createDate 2025-05-10 11:26:36
 */
@Service
public class UserStatServiceImpl extends ServiceImpl<UserStatMapper, UserStat>
        implements UserStatService {

}




