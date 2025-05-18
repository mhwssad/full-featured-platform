package com.example.cybz_back.entity.mysql.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @TableName permission
 */
@TableName(value = "permission")
@Data
public class Permission implements Serializable {
    /**
     * 权限ID
     */
    @TableId(type = IdType.AUTO)
    private Long permId;

    /**
     * 权限代码（如：user:delete）
     */
    private String permCode;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 权限分组
     */
    private String permGroup;

    /**
     * 权限描述
     */
    private String permDesc;

    /**
     * 是否系统内置
     */
    private Integer isSystem;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDate createdTime;

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
        Permission other = (Permission) that;
        return (this.getPermId() == null ? other.getPermId() == null : this.getPermId().equals(other.getPermId()))
                && (this.getPermCode() == null ? other.getPermCode() == null : this.getPermCode().equals(other.getPermCode()))
                && (this.getPermName() == null ? other.getPermName() == null : this.getPermName().equals(other.getPermName()))
                && (this.getPermGroup() == null ? other.getPermGroup() == null : this.getPermGroup().equals(other.getPermGroup()))
                && (this.getPermDesc() == null ? other.getPermDesc() == null : this.getPermDesc().equals(other.getPermDesc()))
                && (this.getIsSystem() == null ? other.getIsSystem() == null : this.getIsSystem().equals(other.getIsSystem()))
                && (this.getIsEnabled() == null ? other.getIsEnabled() == null : this.getIsEnabled().equals(other.getIsEnabled()))
                && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPermId() == null) ? 0 : getPermId().hashCode());
        result = prime * result + ((getPermCode() == null) ? 0 : getPermCode().hashCode());
        result = prime * result + ((getPermName() == null) ? 0 : getPermName().hashCode());
        result = prime * result + ((getPermGroup() == null) ? 0 : getPermGroup().hashCode());
        result = prime * result + ((getPermDesc() == null) ? 0 : getPermDesc().hashCode());
        result = prime * result + ((getIsSystem() == null) ? 0 : getIsSystem().hashCode());
        result = prime * result + ((getIsEnabled() == null) ? 0 : getIsEnabled().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", permId=").append(permId);
        sb.append(", permCode=").append(permCode);
        sb.append(", permName=").append(permName);
        sb.append(", permGroup=").append(permGroup);
        sb.append(", permDesc=").append(permDesc);
        sb.append(", isSystem=").append(isSystem);
        sb.append(", isEnabled=").append(isEnabled);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}