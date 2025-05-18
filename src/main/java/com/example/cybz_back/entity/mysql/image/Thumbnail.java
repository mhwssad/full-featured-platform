package com.example.cybz_back.entity.mysql.image;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName thumbnail
 */
@TableName(value = "thumbnail")
@Data
public class Thumbnail implements Serializable {
    /**
     * 缩略图ID
     */
    @TableId(type = IdType.AUTO)
    private Long thumbId;

    /**
     * 原图ID（外键image.image_id）
     */
    private Long imageId;

    /**
     * 目标宽度（像素）
     */
    private Integer width;

    /**
     * 目标高度（像素）
     */
    private Integer height;

    /**
     * 压缩质量（1-100）
     */
    private Integer quality;

    /**
     * 缩略图文件MD5
     */
    private String hashMd5;

    /**
     * 存储路径
     */
    private String storagePath;

    /**
     * 生成时间
     */
    private Date generatedTime;

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
        Thumbnail other = (Thumbnail) that;
        return (this.getThumbId() == null ? other.getThumbId() == null : this.getThumbId().equals(other.getThumbId()))
                && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
                && (this.getWidth() == null ? other.getWidth() == null : this.getWidth().equals(other.getWidth()))
                && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
                && (this.getQuality() == null ? other.getQuality() == null : this.getQuality().equals(other.getQuality()))
                && (this.getHashMd5() == null ? other.getHashMd5() == null : this.getHashMd5().equals(other.getHashMd5()))
                && (this.getStoragePath() == null ? other.getStoragePath() == null : this.getStoragePath().equals(other.getStoragePath()))
                && (this.getGeneratedTime() == null ? other.getGeneratedTime() == null : this.getGeneratedTime().equals(other.getGeneratedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getThumbId() == null) ? 0 : getThumbId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getWidth() == null) ? 0 : getWidth().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getQuality() == null) ? 0 : getQuality().hashCode());
        result = prime * result + ((getHashMd5() == null) ? 0 : getHashMd5().hashCode());
        result = prime * result + ((getStoragePath() == null) ? 0 : getStoragePath().hashCode());
        result = prime * result + ((getGeneratedTime() == null) ? 0 : getGeneratedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", thumbId=").append(thumbId);
        sb.append(", imageId=").append(imageId);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", quality=").append(quality);
        sb.append(", hashMd5=").append(hashMd5);
        sb.append(", storagePath=").append(storagePath);
        sb.append(", generatedTime=").append(generatedTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}