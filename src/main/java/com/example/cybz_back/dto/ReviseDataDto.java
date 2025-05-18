package com.example.cybz_back.dto;

import com.example.cybz_back.enumerate.GenderEnum;
import com.example.cybz_back.validation.annotation.Privacy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviseDataDto {

    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 隐私设置: public/private/friends
     */
    @Privacy
    private String privacy;
}
