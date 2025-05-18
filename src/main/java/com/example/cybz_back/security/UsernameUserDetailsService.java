package com.example.cybz_back.security;


import com.example.cybz_back.entity.mysql.auth.AuthMethod;
import com.example.cybz_back.service.cybz.user.PermissionsManagementService;
import com.example.cybz_back.service.mysql.auth.AuthMethodService;
import com.example.cybz_back.service.mysql.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.cybz_back.security.MultiAuthUserDetailsService.getUserDetails;

@RequiredArgsConstructor
@Service
public class UsernameUserDetailsService implements UserDetailsService {
    private final AuthMethodService authMethodService;

    private final UsersService usersService;

    private final PermissionsManagementService permissionsManagementService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthMethod byAuthKey = authMethodService.getByAuthKey(username);
        if (byAuthKey == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getUserDetails(byAuthKey, usersService, permissionsManagementService);
    }
}
