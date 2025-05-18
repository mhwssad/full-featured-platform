package com.example.cybz_back.controller.user;

import com.example.cybz_back.dto.ReviseDataDto;
import com.example.cybz_back.dto.ReviseImageDao;
import com.example.cybz_back.entity.mysql.user.Users;
import com.example.cybz_back.security.SecurityUser;
import com.example.cybz_back.service.mysql.user.UsersService;
import com.example.cybz_back.utils.JSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户数据接口")
public class UserDataController {
    private final UsersService usersService;

    @Operation(summary = "获取用户数据")
    @Parameters(
            value = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "userId",
                            description = "用户ID",
                            required = true
                    )
            }
    )
    @GetMapping("/me")
    public JSONResult<Users> getUserData() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getByCacheUserID(principal.getUserId());
        return JSONResult.ok(user);
    }

    @Operation(summary = "修改用户数据")
    @PostMapping("/revise")
    public JSONResult<Void> reviseUserData(@RequestBody @Valid ReviseDataDto reviseDataDto) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviseDataDto.setUserId(principal.getUserId());
        usersService.updateByReviseData(reviseDataDto);
        return JSONResult.ok();
    }

    @Operation(summary = "修改用户头像")
    @PostMapping("/avatar")
    public JSONResult<Void> reviseAvatar(ReviseImageDao avatar) throws IOException {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avatar.setUserId(principal.getUserId());
        usersService.updateByAvatar(avatar);
        return JSONResult.ok();
    }

    @Operation(summary = "修改用户背景")
    @PostMapping("/background")
    public JSONResult<Void> reviseBackground(ReviseImageDao background) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        background.setUserId(principal.getUserId());
        usersService.updateByBackground(background);
        return JSONResult.ok();
    }
}


