package com.example.cybz_back.entity.mysql.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@TableName(value = "user_stat")
@Data
public class UserStat implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 作品数
     */
    private Integer opus;

    /**
     * 粉丝数
     */
    private Long followerCount;

    /**
     * 关注数
     */
    private Long followCount;

    /**
     * 获赞数
     */
    private Long likeCount;

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
        UserStat other = (UserStat) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getOpus() == null ? other.getOpus() == null : this.getOpus().equals(other.getOpus()))
                && (this.getFollowerCount() == null ? other.getFollowerCount() == null : this.getFollowerCount().equals(other.getFollowerCount()))
                && (this.getFollowCount() == null ? other.getFollowCount() == null : this.getFollowCount().equals(other.getFollowCount()))
                && (this.getLikeCount() == null ? other.getLikeCount() == null : this.getLikeCount().equals(other.getLikeCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOpus() == null) ? 0 : getOpus().hashCode());
        result = prime * result + ((getFollowerCount() == null) ? 0 : getFollowerCount().hashCode());
        result = prime * result + ((getFollowCount() == null) ? 0 : getFollowCount().hashCode());
        result = prime * result + ((getLikeCount() == null) ? 0 : getLikeCount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", userId=" + userId +
                ", opus=" + opus +
                ", followerCount=" + followerCount +
                ", followCount=" + followCount +
                ", likeCount=" + likeCount +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}