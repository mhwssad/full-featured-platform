package com.example.cybz_back.service.mysql.user.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.component.mybatis.ImageComponent;
import com.example.cybz_back.dto.ReviseDataDto;
import com.example.cybz_back.dto.ReviseImageDao;
import com.example.cybz_back.entity.mysql.image.Image;
import com.example.cybz_back.entity.mysql.user.Users;
import com.example.cybz_back.mapper.UsersMapper;
import com.example.cybz_back.service.mysql.image.ImageService;
import com.example.cybz_back.service.mysql.user.UsersService;
import com.example.cybz_back.utils.FileHashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author liujian
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-05-10 11:26:36
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    private final ImageComponent imageComponent;
    private final ImageService imageService;

    @Value("${default.file.image.avatar}")
    private String avatarPath;
    @Value("${default.file.image.background}")
    private String backgroundPath;

    @Value("${default.file.image.test}")
    private String testPath;

    @Override
    public Users addUser() {
        Users users = new Users();
        save(users);
        return users;
    }

    @Override
    public Users getByUserId(Long userId) {
        return new LambdaQueryChainWrapper<>(baseMapper).eq(Users::getUserId, userId).one();
    }

    @Override
    @Cacheable(value = "user", key = "#username")  // 添加
    public Users getByCacheUserID(Long username) {
        return getByUserId(username);
    }

    @Override
    @CacheEvict(value = "user", key = "#user.userId")  // 删除
//    @CachePut(value = "user", key = "#user.username")  // 更新
    public void updateByReviseData(ReviseDataDto user) {
        Users convert = Convert.convert(Users.class, user);
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Users::getUserId, user.getUserId())
                .update(convert);
    }

    public void updateAvatar(Long userId, String byOldMd5) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Users::getUserId, userId)
                .set(Users::getAvatar, byOldMd5)
                .update();
    }

    @Override
    public void updateByAvatar(ReviseImageDao avatar) throws IOException {
        // 2. 保存原始文件到临时目录
        Path tempFilePath = savaAvatar(avatar);

        try {
            // 3. 计算哈希值
            String sha256 = FileHashUtils.sha256(tempFilePath);
            String md5 = FileHashUtils.md5(tempFilePath);
            // 4. 判断是否已经存在
            Image byOldMd5 = imageService.getByUpdateMd5(md5, sha256);
            if (byOldMd5 != null) {
                updateAvatar(avatar.getUserId(), byOldMd5.getStoragePath());
                Users byCacheUserID = getByCacheUserID(avatar.getUserId());
                imageService.updateBySubtractReferenceCount(byCacheUserID.getAvatar());
                return;
            }

            // 6. 生成目标路径
            String fileName = sha256 + ".jpg";
            Path targetPath = Paths.get(backgroundPath, fileName);

            // 7. 异步转换并更新数据库（完成后删除临时文件）
            imageComponent.convertWithQuality(tempFilePath.toFile(), targetPath.toFile())
                    .thenAccept(result -> updateAvatar(avatar.getUserId(), String.valueOf(targetPath)))
                    .whenComplete((result, ex) -> {
                        // 无论成功还是异常，都删除临时文件
                        try {
                            Files.deleteIfExists(tempFilePath);
                            log.info("临时文件已删除: {}", tempFilePath);
                        } catch (IOException e) {
                            log.error("删除临时文件失败: {}", tempFilePath, e);
                        }
                    });
        } catch (Exception e) {
            // 同步代码中的异常立即删除临时文件
            Files.deleteIfExists(tempFilePath);
            throw e;
        }
    }

    private Path savaAvatar(ReviseImageDao avatar) throws IOException {
        MultipartFile avatarFile = avatar.getAvatar();
        String originalFilename = avatarFile.getOriginalFilename();

        // 1. 文件名安全检查
        if (originalFilename.contains("..") || originalFilename.contains("/")) {
            throw new SecurityException("文件名包含非法字符");
        }
        Path tempDir = Paths.get(testPath);
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
        Path tempFilePath = tempDir.resolve(originalFilename);
        avatarFile.transferTo(tempFilePath);
        return tempFilePath;
    }

    @Override
    public void updateByBackground(ReviseImageDao background) {

    }
}




