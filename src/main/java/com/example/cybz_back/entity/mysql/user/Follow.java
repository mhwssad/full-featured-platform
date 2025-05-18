package com.example.cybz_back.entity.mysql.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表示用户关注关系的实体类，每个实例代表一个关注事件
 * 包含关注者ID、被关注者ID和关注时间等信息
 */
@TableName(value = "follow")
@Data
@AllArgsConstructor  // 添加了构造函数，用于创建具有指定值的Follow对象
@NoArgsConstructor  // 添加了无参构造函数，用于创建一个空的Follow对象
public class Follow implements Serializable {
    /**
     * 关注者ID
     */
    @TableId
    private Long followerId;

    /**
     * 被关注者ID
     */
    private Long followedId;

    /**
     * 关注时间（精确到毫秒）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 序列化ID，用于实现Serializable接口，保证序列化的一致性和兼容性
     */
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 重写equals方法，比较两个Follow对象是否相等
     * 主要通过比较followerId、followedId和createdAt来确定对象是否相等
     *
     * @param that 另一个对象，用于与当前对象进行比较
     * @return 如果两个对象相等则返回true，否则返回false
     */
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
        Follow other = (Follow) that;
        return (this.getFollowerId() == null ? other.getFollowerId() == null : this.getFollowerId().equals(other.getFollowerId()))
                && (this.getFollowedId() == null ? other.getFollowedId() == null : this.getFollowedId().equals(other.getFollowedId()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    /**
     * 重写hashCode方法，生成当前对象的哈希码
     * 主要基于followerId、followedId和createdAt计算哈希值
     *
     * @return 当前对象的哈希码
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFollowerId() == null) ? 0 : getFollowerId().hashCode());
        result = prime * result + ((getFollowedId() == null) ? 0 : getFollowedId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    /**
     * 重写toString方法，返回包含关注关系信息的字符串
     * 主要包括类名、哈希码、followerId、followedId、createdAt和serialVersionUID等信息
     *
     * @return 包含关注关系信息的字符串
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", followerId=" + followerId +
                ", followedId=" + followedId +
                ", createdAt=" + createdAt +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}
