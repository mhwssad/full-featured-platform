package com.example.cybz_back.dto;

import com.example.cybz_back.validation.annotation.NotMultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "修改用户头像或背景")
public class ReviseImageDao {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户头像")
    @NotMultipartFile(groups = {ReviseAvatar.class})
    private MultipartFile avatar;

    @Schema(description = "用户背景")
    @NotMultipartFile(groups = {ReviseBackground.class})
    private MultipartFile background;

    interface ReviseAvatar {

    }

    interface ReviseBackground {

    }
}
