package com.example.cybz_back.dto;

import com.example.cybz_back.validation.annotation.NotMultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviseImageDao {
    private Long userId;

    @NotMultipartFile(groups = {ReviseAvatar.class})
    private MultipartFile avatar;

    @NotMultipartFile(groups = {ReviseBackground.class})
    private MultipartFile background;

    interface ReviseAvatar {

    }

    interface ReviseBackground {

    }
}
