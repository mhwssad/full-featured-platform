package com.example.cybz_back.dto;

import com.example.cybz_back.enumerate.GenderEnum;
import com.example.cybz_back.validation.annotation.Privacy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "修改用户数据")
public class ReviseDataDto {

    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 个性签名
     */
    @Schema(description = "个性签名")
    private String signature;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private GenderEnum gender;

    /**
     * 生日
     */
    @Schema(description = "生日")
    private LocalDate birthday;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 隐私设置: public/private/friends
     */
    @Schema(description = "隐私设置: public/private/friends")
    @Privacy
    private String privacy;
}
