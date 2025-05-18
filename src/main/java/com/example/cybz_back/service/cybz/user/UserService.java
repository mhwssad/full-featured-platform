package com.example.cybz_back.service.cybz.user;

import com.example.cybz_back.utils.JSONResult;

public interface UserService {
    JSONResult<?> login(String username, String password, String request);

    JSONResult<?> captcha(String email, String captcha, String request);
}
