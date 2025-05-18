package com.example.cybz_back.service.mysql.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.auth.AuthType;
import com.example.cybz_back.mapper.AuthTypeMapper;
import com.example.cybz_back.service.mysql.auth.AuthTypeService;
import org.springframework.stereotype.Service;

/**
 * @author liujian
 * @description 针对表【auth_type】的数据库操作Service实现
 * @createDate 2025-05-10 13:02:35
 */
@Service
public class AuthTypeServiceImpl extends ServiceImpl<AuthTypeMapper, AuthType>
        implements AuthTypeService {

}




