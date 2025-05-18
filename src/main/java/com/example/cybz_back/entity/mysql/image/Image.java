package com.example.cybz_back.entity.mysql.image;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName image
 */
@TableName(value = "image")
@Data
@AllArgsConstructor
public class Image implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long imageId;

    /**
     * 原始文件MD5
     */
    private String originalMd5;

    /**
     * 原始文件SHA256
     */
    private String originalSha256;

    /**
     * 原始格式（如png/webp）
     */
    private String originalFormat;

    /**
     * 原始文件字节数
     */
    private Long originalSize;

    /**
     * 转换后JPG的MD5
     */
    private String convertedMd5;

    /**
     * 转换后JPG的SHA256
     */
    private String convertedSha256;

    /**
     * 转换后文件字节数
     */
    private Long convertedSize;

    /**
     * 转换后文件存储路径
     */
    private String storagePath;

    /**
     * 原始文件元数据（EXIF/IPTC等）
     */
    private Object originalMetadata;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     *
     */
    private Date uploadTime;

    /**
     * 总引用次数
     */
    private Integer referenceCount;

    /**
     * 0=已删除 1=正常
     */
    private Integer status;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Image other = (Image) that;
        return (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
                && (this.getOriginalMd5() == null ? other.getOriginalMd5() == null : this.getOriginalMd5().equals(other.getOriginalMd5()))
                && (this.getOriginalSha256() == null ? other.getOriginalSha256() == null : this.getOriginalSha256().equals(other.getOriginalSha256()))
                && (this.getOriginalFormat() == null ? other.getOriginalFormat() == null : this.getOriginalFormat().equals(other.getOriginalFormat()))
                && (this.getOriginalSize() == null ? other.getOriginalSize() == null : this.getOriginalSize().equals(other.getOriginalSize()))
                && (this.getConvertedMd5() == null ? other.getConvertedMd5() == null : this.getConvertedMd5().equals(other.getConvertedMd5()))
                && (this.getConvertedSha256() == null ? other.getConvertedSha256() == null : this.getConvertedSha256().equals(other.getConvertedSha256()))
                && (this.getConvertedSize() == null ? other.getConvertedSize() == null : this.getConvertedSize().equals(other.getConvertedSize()))
                && (this.getStoragePath() == null ? other.getStoragePath() == null : this.getStoragePath().equals(other.getStoragePath()))
                && (this.getOriginalMetadata() == null ? other.getOriginalMetadata() == null : this.getOriginalMetadata().equals(other.getOriginalMetadata()))
                && (this.getUploaderId() == null ? other.getUploaderId() == null : this.getUploaderId().equals(other.getUploaderId()))
                && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
                && (this.getReferenceCount() == null ? other.getReferenceCount() == null : this.getReferenceCount().equals(other.getReferenceCount()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getOriginalMd5() == null) ? 0 : getOriginalMd5().hashCode());
        result = prime * result + ((getOriginalSha256() == null) ? 0 : getOriginalSha256().hashCode());
        result = prime * result + ((getOriginalFormat() == null) ? 0 : getOriginalFormat().hashCode());
        result = prime * result + ((getOriginalSize() == null) ? 0 : getOriginalSize().hashCode());
        result = prime * result + ((getConvertedMd5() == null) ? 0 : getConvertedMd5().hashCode());
        result = prime * result + ((getConvertedSha256() == null) ? 0 : getConvertedSha256().hashCode());
        result = prime * result + ((getConvertedSize() == null) ? 0 : getConvertedSize().hashCode());
        result = prime * result + ((getStoragePath() == null) ? 0 : getStoragePath().hashCode());
        result = prime * result + ((getOriginalMetadata() == null) ? 0 : getOriginalMetadata().hashCode());
        result = prime * result + ((getUploaderId() == null) ? 0 : getUploaderId().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getReferenceCount() == null) ? 0 : getReferenceCount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", imageId=").append(imageId);
        sb.append(", originalMd5=").append(originalMd5);
        sb.append(", originalSha256=").append(originalSha256);
        sb.append(", originalFormat=").append(originalFormat);
        sb.append(", originalSize=").append(originalSize);
        sb.append(", convertedMd5=").append(convertedMd5);
        sb.append(", convertedSha256=").append(convertedSha256);
        sb.append(", convertedSize=").append(convertedSize);
        sb.append(", storagePath=").append(storagePath);
        sb.append(", originalMetadata=").append(originalMetadata);
        sb.append(", uploaderId=").append(uploaderId);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", referenceCount=").append(referenceCount);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}