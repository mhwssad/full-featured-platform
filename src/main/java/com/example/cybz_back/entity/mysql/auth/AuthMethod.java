package com.example.cybz_back.entity.mysql.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName auth_method
 */
@TableName(value = "auth_method")
@Data
public class AuthMethod implements Serializable {
    /**
     * 认证方式ID
     */
    @TableId(type = IdType.AUTO)
    private Long authId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 登录凭证
     */
    private String authKey;

    /**
     * 加密凭证
     */
    private String authSecret;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 凭证过期时间
     */
    private Date expiredTime;

    /**
     * 最后使用时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date lastUsedTime;

    /**
     * 最后使用IP
     */
    private String lastUsedIp;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedTime;

    /**
     * 认证方式配置元数据
     */
    private Object metadata;

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
        AuthMethod other = (AuthMethod) that;
        return (this.getAuthId() == null ? other.getAuthId() == null : this.getAuthId().equals(other.getAuthId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getAuthType() == null ? other.getAuthType() == null : this.getAuthType().equals(other.getAuthType()))
                && (this.getAuthKey() == null ? other.getAuthKey() == null : this.getAuthKey().equals(other.getAuthKey()))
                && (this.getAuthSecret() == null ? other.getAuthSecret() == null : this.getAuthSecret().equals(other.getAuthSecret()))
                && (this.getIsActive() == null ? other.getIsActive() == null : this.getIsActive().equals(other.getIsActive()))
                && (this.getExpiredTime() == null ? other.getExpiredTime() == null : this.getExpiredTime().equals(other.getExpiredTime()))
                && (this.getLastUsedTime() == null ? other.getLastUsedTime() == null : this.getLastUsedTime().equals(other.getLastUsedTime()))
                && (this.getLastUsedIp() == null ? other.getLastUsedIp() == null : this.getLastUsedIp().equals(other.getLastUsedIp()))
                && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
                && (this.getUpdatedTime() == null ? other.getUpdatedTime() == null : this.getUpdatedTime().equals(other.getUpdatedTime()))
                && (this.getMetadata() == null ? other.getMetadata() == null : this.getMetadata().equals(other.getMetadata()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAuthId() == null) ? 0 : getAuthId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAuthType() == null) ? 0 : getAuthType().hashCode());
        result = prime * result + ((getAuthKey() == null) ? 0 : getAuthKey().hashCode());
        result = prime * result + ((getAuthSecret() == null) ? 0 : getAuthSecret().hashCode());
        result = prime * result + ((getIsActive() == null) ? 0 : getIsActive().hashCode());
        result = prime * result + ((getExpiredTime() == null) ? 0 : getExpiredTime().hashCode());
        result = prime * result + ((getLastUsedTime() == null) ? 0 : getLastUsedTime().hashCode());
        result = prime * result + ((getLastUsedIp() == null) ? 0 : getLastUsedIp().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdatedTime() == null) ? 0 : getUpdatedTime().hashCode());
        result = prime * result + ((getMetadata() == null) ? 0 : getMetadata().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", authId=").append(authId);
        sb.append(", userId=").append(userId);
        sb.append(", authType=").append(authType);
        sb.append(", authKey=").append(authKey);
        sb.append(", authSecret=").append(authSecret);
        sb.append(", isActive=").append(isActive);
        sb.append(", expiredTime=").append(expiredTime);
        sb.append(", lastUsedTime=").append(lastUsedTime);
        sb.append(", lastUsedIp=").append(lastUsedIp);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", metadata=").append(metadata);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}