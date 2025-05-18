package com.example.cybz_back.service.mysql.image.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cybz_back.entity.mysql.image.Image;
import com.example.cybz_back.mapper.ImageMapper;
import com.example.cybz_back.service.mysql.image.ImageService;
import org.springframework.stereotype.Service;

/**
 * @author liujian
 * @description 针对表【image】的数据库操作Service实现
 * @createDate 2025-05-17 15:02:36
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
        implements ImageService {

    @Override
    public Image getByUpdateMd5(String md5, String sha256) {
        Image one = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(Image::getOriginalMd5, md5)
                .eq(Image::getOriginalSha256, sha256)
                .one();
        if (one != null) {
            updateByAddReferenceCount(one.getImageId());
        }
        return one;
    }

    @Override
    public void updateByAddReferenceCount(Long imageId) {
        new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(Image::getImageId, imageId)
                .setSql("reference_count = reference_count + 1")
                .update();
    }

    public Image getByAvatar(String imagePath) {
        return new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(Image::getStoragePath, imagePath)
                .one();
    }

    @Override
    public void updateBySubtractReferenceCount(String imagePath) {
        Image byAvatar = getByAvatar(imagePath);
        new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(Image::getImageId, byAvatar.getImageId())
                .setSql("reference_count = reference_count - 1")
                .update();
    }
}




