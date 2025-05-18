package com.example.cybz_back.entity.mysql.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@TableName(value = "user_token")
@Data
public class UserToken implements Serializable {
    /**
     * 令牌ID
     */
    @TableId(type = IdType.AUTO)
    private Long tokenId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 令牌
     */
    private String token;

    /**
     * 令牌哈希（全大写存储）
     */
    private String tokenHash;

    /**
     * 过期时间
     */
    private LocalDate expiredTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDate createdTime;

    /**
     * 最后使用时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDate lastUsedTime;

    /**
     * 最后使用时IP
     */
    private String lastUsedIp;

    /**
     * 令牌类型
     */
    private Object tokenType;

    /**
     * 令牌状态
     */
    private Object status;

    /**
     * 激活标志（1表示有效）
     */
    private Integer activeFlag;

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
        UserToken other = (UserToken) that;
        return (this.getTokenId() == null ? other.getTokenId() == null : this.getTokenId().equals(other.getTokenId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getToken() == null ? other.getToken() == null : this.getToken().equals(other.getToken()))
                && (this.getTokenHash() == null ? other.getTokenHash() == null : this.getTokenHash().equals(other.getTokenHash()))
                && (this.getExpiredTime() == null ? other.getExpiredTime() == null : this.getExpiredTime().equals(other.getExpiredTime()))
                && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
                && (this.getLastUsedTime() == null ? other.getLastUsedTime() == null : this.getLastUsedTime().equals(other.getLastUsedTime()))
                && (this.getLastUsedIp() == null ? other.getLastUsedIp() == null : this.getLastUsedIp().equals(other.getLastUsedIp()))
                && (this.getTokenType() == null ? other.getTokenType() == null : this.getTokenType().equals(other.getTokenType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getActiveFlag() == null ? other.getActiveFlag() == null : this.getActiveFlag().equals(other.getActiveFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTokenId() == null) ? 0 : getTokenId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        result = prime * result + ((getTokenHash() == null) ? 0 : getTokenHash().hashCode());
        result = prime * result + ((getExpiredTime() == null) ? 0 : getExpiredTime().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getLastUsedTime() == null) ? 0 : getLastUsedTime().hashCode());
        result = prime * result + ((getLastUsedIp() == null) ? 0 : getLastUsedIp().hashCode());
        result = prime * result + ((getTokenType() == null) ? 0 : getTokenType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getActiveFlag() == null) ? 0 : getActiveFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tokenId=").append(tokenId);
        sb.append(", userId=").append(userId);
        sb.append(", token=").append(token);
        sb.append(", tokenHash=").append(tokenHash);
        sb.append(", expiredTime=").append(expiredTime);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", lastUsedTime=").append(lastUsedTime);
        sb.append(", lastUsedIp=").append(lastUsedIp);
        sb.append(", tokenType=").append(tokenType);
        sb.append(", status=").append(status);
        sb.append(", activeFlag=").append(activeFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}