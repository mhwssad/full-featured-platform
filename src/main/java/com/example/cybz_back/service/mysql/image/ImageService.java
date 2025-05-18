package com.example.cybz_back.service.mysql.image;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cybz_back.entity.mysql.image.Image;

/**
 * @author liujian
 * @description 针对表【image】的数据库操作Service
 * @createDate 2025-05-17 15:02:36
 */
public interface ImageService extends IService<Image> {

    Image getByUpdateMd5(String md5, String sha256);

    void updateByAddReferenceCount(Long imageId);

    void updateBySubtractReferenceCount(String imagePath);
}
