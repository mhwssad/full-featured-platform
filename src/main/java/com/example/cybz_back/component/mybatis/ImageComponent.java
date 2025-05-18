package com.example.cybz_back.component.mybatis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
public class ImageComponent {

    @Value("${default.file.image.processing.quality}")
    private float quality;

    @Async("taskExecutor")
    public CompletableFuture<Void> convertWithQuality(File input, File output) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BufferedImage image = ImageIO.read(input);
                if (image == null) {
                    throw new IOException("无法读取图片文件");
                }

                // 处理透明背景
                BufferedImage rgbImage = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
                Graphics2D graphics = rgbImage.createGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, image.getWidth(), image.getHeight());
                graphics.drawImage(image, 0, 0, null);
                graphics.dispose();

                // 校验质量参数
                float validQuality = Math.max(0.1f, Math.min(1.0f, quality));

                // 写入文件
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                ImageWriter writer = writers.next();
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
                    ImageWriteParam param = writer.getDefaultWriteParam();
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(validQuality);

                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(rgbImage, null, null), param);
                } finally {
                    writer.dispose();
                }

                return null;
            } catch (Exception e) {
                throw new CompletionException(e); // 异常封装
            }
        });
    }
}
