package com.example.cybz_back.controller.user;

import com.example.cybz_back.dto.ReviseDataDto;
import com.example.cybz_back.dto.ReviseImageDao;
import com.example.cybz_back.entity.mysql.user.Users;
import com.example.cybz_back.security.SecurityUser;
import com.example.cybz_back.service.mysql.user.UsersService;
import com.example.cybz_back.utils.JSONResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserDataController {
    private final UsersService usersService;


    @GetMapping("/me")
    public JSONResult<Users> getUserData() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getByCacheUserID(principal.getUserId());
        return JSONResult.ok(user);
    }

    @PostMapping("/revise")
    public JSONResult<Void> reviseUserData(@RequestBody @Valid ReviseDataDto reviseDataDto) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviseDataDto.setUserId(principal.getUserId());
        usersService.updateByReviseData(reviseDataDto);
        return JSONResult.ok();
    }

    @PostMapping("/avatar")
    public JSONResult<Void> reviseAvatar(ReviseImageDao avatar) throws IOException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avatar.setUserId(principal.getUserId());
        usersService.updateByAvatar(avatar);
        return JSONResult.ok();
    }

    @PostMapping("/background")
    public JSONResult<Void> reviseBackground(ReviseImageDao background) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        background.setUserId(principal.getUserId());
        usersService.updateByBackground(background);
        return JSONResult.ok();
    }
}


