-- 创建数据库（显式指定字符集）
CREATE DATABASE if not exists cybz
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- 选择数据库
USE cybz;

-- ----------------------------
-- 用户相关
-- ----------------------------
-- 用户表
CREATE TABLE `users`
(
    `user_id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `enabled`                 TINYINT(1)                                DEFAULT 1 COMMENT '是否启用',
    `account_non_expired`     TINYINT(1)                                DEFAULT 1 COMMENT '账号是否未过期',
    `account_non_locked`      TINYINT(1)                                DEFAULT 1 COMMENT '账号是否未锁定',
    `credentials_non_expired` TINYINT(1)                                DEFAULT 1 COMMENT '凭证是否未过期',
    `avatar`                  VARCHAR(255)                     NOT NULL DEFAULT '/avatar/75622209a4794d5fb1060c4bd8ff18fa.jpg' COMMENT '用户头像',
    `nickname`                VARCHAR(50)                      NOT NULL DEFAULT 'cybzacg' COMMENT '用户昵称',
    `background_image`        VARCHAR(255)                     NOT NULL DEFAULT '/background/75622209a4794d5fb1060c4bd8ff18fa.jpg' COMMENT '用户背景图',
    `signature`               VARCHAR(255)                     NOT NULL DEFAULT '这个家伙很懒，什么都没有留下' COMMENT '个性签名',
    `gender`                  ENUM ('man', 'woman', 'unknown') NOT NULL DEFAULT 'unknown' COMMENT '性别',
    `birthday`                DATE                                      DEFAULT NULL COMMENT '生日',
    `address`                 VARCHAR(255)                              DEFAULT NULL COMMENT '地址',
    `privacy`                 VARCHAR(20)                      NOT NULL DEFAULT 'public' COMMENT '隐私设置: public/private/friends',
    `created_time`            DATETIME                                  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`            DATETIME                                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 索引
    INDEX idx_nickname (`nickname`(20)) COMMENT '昵称前缀索引（兼顾存储与查询效率）',
    INDEX idx_created_time (`created_time`) COMMENT '注册时间排序索引',
    INDEX idx_gender_birthday (`gender`, `birthday`) COMMENT '性别+生日联合索引',
    INDEX idx_privacy_status (`privacy`, `enabled`) COMMENT '隐私状态+账户状态联合索引',
    INDEX idx_time_combination (`created_time`, `updated_time`) COMMENT '时间维度联合索引',
    INDEX idx_address_prefix (`address`(15)) COMMENT '地址前缀索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 图片管理表
CREATE TABLE `image`
(
    `image_id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    -- 原始文件特征
    `original_md5`      CHAR(32)     NOT NULL COMMENT '原始文件MD5',
    `original_sha256`   CHAR(64)     NOT NULL COMMENT '原始文件SHA256',
    `original_format`   VARCHAR(10)  NOT NULL COMMENT '原始格式（如png/webp）',
    `original_size`     BIGINT       NOT NULL COMMENT '原始文件字节数',
    -- 转换后文件特征
    `converted_md5`     CHAR(32)     NOT NULL COMMENT '转换后JPG的MD5',
    `converted_sha256`  CHAR(64)     NOT NULL COMMENT '转换后JPG的SHA256',
    `converted_size`    BIGINT       NOT NULL COMMENT '转换后文件字节数',
    -- 存储信息
    `storage_path`      VARCHAR(512) NOT NULL COMMENT '转换后文件存储路径',
    `original_metadata` JSON COMMENT '原始文件元数据（EXIF/IPTC等）',
    -- 管理字段
    `uploader_id`       BIGINT       NOT NULL COMMENT '上传用户ID',
    `upload_time`       DATETIME DEFAULT CURRENT_TIMESTAMP,
    `reference_count`   INT      DEFAULT 1 COMMENT '总引用次数',
    `status`            TINYINT  DEFAULT 1 COMMENT '0=已删除 1=正常',

    -- 唯一约束
    UNIQUE KEY `uk_original_file` (`original_md5`, `original_sha256`),
    UNIQUE KEY `uk_converted_file` (`converted_md5`, `converted_sha256`),

    -- 索引
    KEY `idx_uploader` (`uploader_id`),
    KEY `idx_converted_hash` (`converted_md5`(8), `converted_sha256`(12))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 缩略图
CREATE TABLE `thumbnail`
(
    `thumb_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '缩略图ID',
    `image_id`       BIGINT            NOT NULL COMMENT '原图ID（外键image.image_id）',
    `width`          SMALLINT UNSIGNED NOT NULL COMMENT '目标宽度（像素）',
    `height`         SMALLINT UNSIGNED NOT NULL COMMENT '目标高度（像素）',
    `quality`        TINYINT UNSIGNED DEFAULT 85 COMMENT '压缩质量（1-100）',
    `hash_md5`       CHAR(32)          NOT NULL COMMENT '缩略图文件MD5',
    `storage_path`   VARCHAR(512)      NOT NULL COMMENT '存储路径',
    `generated_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',

    -- 唯一约束（相同参数生成的缩略图唯一）
    UNIQUE KEY `uk_thumb_file` (`image_id`, `width`, `height`, `quality`, `hash_md5`),

    -- 索引
    KEY `idx_parent_image` (`image_id`),
    KEY `idx_dimensions` (`width`, `height`),

    -- 外键约束
    CONSTRAINT `fk_thumb_image`
        FOREIGN KEY (`image_id`)
            REFERENCES `image` (`image_id`)
            ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 用户统计表
CREATE TABLE `user_stat`
(
    `user_id`        BIGINT PRIMARY KEY COMMENT '用户ID',
    `opus`           int unsigned NOT NULL DEFAULT 0 COMMENT '作品数',
    `follower_count` BIGINT       NOT NULL DEFAULT 0 COMMENT '粉丝数',
    `follow_count`   BIGINT       NOT NULL DEFAULT 0 COMMENT '关注数',
    `like_count`     BIGINT       NOT NULL DEFAULT 0 COMMENT '获赞数',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 关注表
CREATE TABLE `follow`
(
    `follower_id` BIGINT      NOT NULL COMMENT '关注者ID',
    `followed_id` BIGINT      NOT NULL COMMENT '被关注者ID',
    `created_at`  DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '关注时间（精确到毫秒）',
    PRIMARY KEY (`follower_id`, `followed_id`), -- 复合主键替代自增ID
    KEY `idx_follower_created` (`follower_id`, `created_at`, `followed_id`) COMMENT '支持关注列表分页',
    KEY `idx_followed_created` (`followed_id`, `created_at`, `follower_id`) COMMENT '支持粉丝列表分页',
    FOREIGN KEY (`follower_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`followed_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- 用户令牌表
CREATE TABLE `user_token`
(
    `token_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '令牌ID',
    `user_id`        BIGINT                           NOT NULL COMMENT '用户ID',
    `token`          VARCHAR(512) COLLATE utf8mb4_bin NOT NULL COMMENT '令牌',
    `token_hash`     VARCHAR(64) GENERATED ALWAYS AS (UPPER(SHA2(token, 256))) STORED COMMENT '令牌哈希（全大写存储）',
    `expired_time`   DATETIME                         NOT NULL COMMENT '过期时间',
    `created_time`   DATETIME                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_used_time` DATETIME                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后使用时间',
    `last_used_ip`   VARCHAR(45)                         DEFAULT NULL COMMENT '最后使用时IP',
    `token_type`     ENUM ('access','refresh')           DEFAULT 'access' COMMENT '令牌类型',
    `status`         ENUM ('active','revoked','expired') DEFAULT 'active' COMMENT '令牌状态',
    `active_flag`    TINYINT(1)                          DEFAULT NULL COMMENT '激活标志（1表示有效）',
    UNIQUE INDEX idx_unique_active_token (`user_id`, `token_type`, `active_flag`),
    UNIQUE INDEX idx_token_hash (`token_hash`),
    INDEX idx_expired_time (`expired_time`),
    INDEX idx_user_status (`user_id`, `status`),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- 创建触发器自动更新active_flag
DELIMITER $$
CREATE TRIGGER set_active_flag_before_insert
    BEFORE INSERT
    ON `user_token`
    FOR EACH ROW
BEGIN
    IF NEW.status = 'active' AND NEW.expired_time > NOW() THEN
        SET NEW.active_flag = 1;
    ELSE
        SET NEW.active_flag = NULL;
    END IF;
END$$
CREATE TRIGGER set_active_flag_before_update
    BEFORE UPDATE
    ON `user_token`
    FOR EACH ROW
BEGIN
    IF NEW.status = 'active' AND NEW.expired_time > NOW() THEN
        SET NEW.active_flag = 1;
    ELSE
        SET NEW.active_flag = NULL;
    END IF;
END$$
DELIMITER ;

-- 积分表
CREATE TABLE `user_point_type`
(
    `type_id`         SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '积分类型ID',
    `type_name`       VARCHAR(20) NOT NULL COMMENT '类型名称（如：金币、银币）',
    `is_active`       TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    `is_exchangeable` TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否允许兑换',
    `created_time`    DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX idx_type_name (`type_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='积分类型配置表';

-- 用户积分表
CREATE TABLE `user_point`
(
    `user_id`      BIGINT            NOT NULL COMMENT '用户ID',
    `type_id`      SMALLINT UNSIGNED NOT NULL COMMENT '积分类型ID',
    `balance`      INT               NOT NULL DEFAULT 0 COMMENT '当前余额',
    `updated_time` DATETIME                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`user_id`, `type_id`), -- 联合主键
    INDEX idx_user_balance (`user_id`, `balance`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`type_id`) REFERENCES `user_point_type` (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户积分账户表';

-- 用户等级表
CREATE TABLE `user_level`
(
    `user_id`       BIGINT PRIMARY KEY COMMENT '用户ID',
    `current_level` SMALLINT UNSIGNED        NOT NULL DEFAULT 1 COMMENT '当前等级',
    `experience`    INT UNSIGNED             NOT NULL DEFAULT 0 COMMENT '累计经验值',
    `level_type`    ENUM ('system','custom') NOT NULL DEFAULT 'system' COMMENT '等级体系类型',
    `updated_time`  DATETIME                          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后升级时间',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    INDEX idx_level_rank (`current_level`, `experience`) COMMENT '等级排行榜索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户等级主表';

-- 积分变动明细表
CREATE TABLE `user_point_log`
(
    `log_id`        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id`       BIGINT                                  NOT NULL COMMENT '用户ID',
    `type_id`       SMALLINT UNSIGNED                       NOT NULL COMMENT '积分类型ID',
    `change_type`   ENUM ('increment','decrement','adjust') NOT NULL COMMENT '变动类型',
    `amount`        INT                                     NOT NULL COMMENT '变动数值',
    `final_balance` INT                                     NOT NULL COMMENT '变动后余额',
    `source`        VARCHAR(30)                             NOT NULL COMMENT '来源（如：sign_in/daily_checkin）',
    `created_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
    `remark`        VARCHAR(100) DEFAULT NULL COMMENT '备注',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`type_id`) REFERENCES `user_point_type` (`type_id`),
    INDEX idx_user_flow (`user_id`, `type_id`, `created_time`) COMMENT '用户分类型流水查询'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='多类型积分流水表';

-- 等级经验流水表
CREATE TABLE `user_exp_log`
(
    `log_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id`      BIGINT       NOT NULL COMMENT '用户ID',
    `change_exp`   INT          NOT NULL COMMENT '经验变化值',
    `source`       VARCHAR(30)  NOT NULL COMMENT '来源（如：video_upload/comment）',
    `final_exp`    INT UNSIGNED NOT NULL COMMENT '变动后总经验',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    INDEX idx_exp_flow (`user_id`, `created_time`) COMMENT '经验流水追踪索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='等级经验变动明细表';

-- ----------------------------
-- 用户认证相关
-- ----------------------------
-- 认证类型字典表
CREATE TABLE `auth_type`
(
    type_code      VARCHAR(20) PRIMARY KEY COMMENT '认证类型代码',
    type_name      VARCHAR(50) NOT NULL COMMENT '认证类型名称',
    description    VARCHAR(255) COMMENT '类型描述',
    `order_num`    INT         NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `is_enabled`   TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用该认证方式',
    `created_time` DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 更新预置数据（补充排序值）
INSERT INTO auth_type (type_code, type_name, description, order_num)
VALUES ('username', '用户名', '传统用户名密码认证', 1),
       ('email', '电子邮箱', '邮箱认证方式', 2),
       ('phone', '手机号', '短信验证码认证', 3),
       ('github', 'GitHub', 'GitHub第三方登录', 4),
       ('wechat', '微信', '微信第三方登录', 5),
       ('qq', 'QQ', 'QQ第三方登录', 6);

-- 认证方式表  区分大小写存储
CREATE TABLE `auth_method`
(
    `auth_id`        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '认证方式ID',
    `user_id`        BIGINT       NOT NULL COMMENT '用户ID',
    `auth_type`      VARCHAR(20)  NOT NULL COMMENT '认证类型',
    `auth_key`       VARCHAR(320) NOT NULL COMMENT '登录凭证',
    `auth_secret`    VARCHAR(512) COMMENT '加密凭证',
    `is_active`      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用',
    `expired_time`   DATETIME COMMENT '凭证过期时间',
    `last_used_time` DATETIME COMMENT '最后使用时间',
    `last_used_ip`   VARCHAR(45) COMMENT '最后使用IP',
    `created_time`   DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `metadata`       JSON COMMENT '认证方式配置元数据',
    -- 唯一索引优化（精确匹配）
    UNIQUE INDEX udx_auth_identifier (`auth_type`, `auth_key`(100)) COMMENT '认证方式唯一约束',
    -- 高频查询索引
    INDEX idx_user_auths (`user_id`, `is_active`, `auth_type`) COMMENT '用户有效认证方式查询',
    INDEX idx_expired_auths (`expired_time`) COMMENT '过期凭证清理索引',

    -- 外键约束调整（移除 ON UPDATE CASCADE）
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`auth_type`) REFERENCES `auth_type` (`type_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 角色表定义
CREATE TABLE `role`
(
    `role_id`      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    `role_code`    VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代码（如：admin/user）',
    `role_name`    VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_desc`    VARCHAR(255) COMMENT '角色描述',
    `created_time` DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_system`    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否系统内置角色',
    `is_enabled`   TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    `is_default`   TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否默认角色',
    INDEX idx_role_status (`is_enabled`, `role_id`) COMMENT '角色状态索引',
    INDEX idx_role_system (`is_system`) COMMENT '系统内置角色索引',
    INDEX idx_role_created (`created_time`) COMMENT '角色创建时间索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 权限表
CREATE TABLE `permission`
(
    `perm_id`      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    `perm_code`    VARCHAR(100) NOT NULL UNIQUE COMMENT '权限代码（如：user:delete）',
    `perm_name`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '权限名称', -- 名称唯一约束
    `perm_group`   VARCHAR(50)  NOT NULL DEFAULT 'default' COMMENT '权限分组',
    `perm_desc`    VARCHAR(255) COMMENT '权限描述',
    `is_system`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否系统内置',
    `is_enabled`   TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用',
    `created_time` DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_perm_group (`perm_group`, `perm_code`) COMMENT '权限分组查询',
    INDEX idx_perm_group_system (`perm_group`, `is_system`) COMMENT '系统内置权限索引',

    INDEX idx_perm_enabled (`is_enabled`, `perm_group`) COMMENT '启用状态过滤',
    INDEX idx_perm_code_prefix (`perm_code`(20)) COMMENT '权限代码前缀检索'

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 用户角色关联表
CREATE TABLE user_role
(
    user_id      BIGINT NOT NULL COMMENT '用户ID',
    role_id      BIGINT NOT NULL COMMENT '角色ID',
    `granted_by` BIGINT COMMENT '授权人', -- 新增审计字段
    `expired_at` DATETIME COMMENT '授权过期时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关联时间',
    PRIMARY KEY (user_id, role_id) COMMENT '用户角色关联表',
    INDEX idx_role_users (role_id, user_id) COMMENT '角色用户反向索引',
    INDEX idx_role_grants (role_id, granted_by) COMMENT '授权记录索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 角色权限关联表
CREATE TABLE role_permission
(
    role_id      BIGINT NOT NULL COMMENT '角色ID',
    perm_id      BIGINT NOT NULL COMMENT '权限ID',
    `granted_by` BIGINT COMMENT '授权人', -- 新增审计字段
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    PRIMARY KEY (role_id, perm_id) COMMENT '角色权限关联表',
    INDEX idx_perm_roles (perm_id, role_id) COMMENT '权限角色反向索引',
    INDEX idx_perm_grants (perm_id, granted_by) COMMENT '授权记录索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- 交互模块优化方案
-- ----------------------------

-- 内容类型字典表（解决ENUM扩展性问题）
CREATE TABLE `content_type`
(
    `type_code` VARCHAR(20) PRIMARY KEY COMMENT '内容类型代码',
    `type_name` VARCHAR(50) NOT NULL COMMENT '类型名称',
    UNIQUE INDEX idx_type_code (`type_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 预置内容类型
INSERT INTO content_type (type_code, type_name)
VALUES ('video', '视频'),
       ('article', '文章'),
       ('product', '商品'),
       ('post', '动态');

-- 评论表优化
CREATE TABLE `comment`
(
    `comment_id`   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `content_type` VARCHAR(20)                          NOT NULL COMMENT '内容类型',
    `content_id`   BIGINT                               NOT NULL COMMENT '内容ID',
    `user_id`      BIGINT                               NOT NULL COMMENT '用户ID',
    `content`      TEXT                                 NOT NULL COMMENT '评论内容（存储处理后的安全文本）',
    `raw_content`  TEXT COMMENT '原始内容（加密存储）',
    `parent_id`    BIGINT COMMENT '父评论ID',
    `like_count`   INT UNSIGNED                         NOT NULL DEFAULT 0 COMMENT '点赞数（缓存计数）',
    `state`        ENUM ('normal','deleted','auditing') NOT NULL DEFAULT 'normal' COMMENT '评论状态',
    `created_at`   DATETIME(3)                          NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `updated_at`   DATETIME(3)                          NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    -- 索引优化
    INDEX idx_main_query (`content_type`, `content_id`, `state`, `created_at`),
    INDEX idx_user_comments (`user_id`, `created_at`),
    INDEX idx_parent (`parent_id`),

    -- 外键约束
    FOREIGN KEY (`content_type`) REFERENCES `content_type` (`type_code`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 评论计数表
CREATE TABLE comment_counter
(
    comment_id  BIGINT PRIMARY KEY,
    like_count  INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    reply_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复数',
    FOREIGN KEY (comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE
);

-- 创建内容互动汇总表
CREATE TABLE content_stats
(
    content_type  VARCHAR(20)     NOT NULL COMMENT '内容类型',
    content_id    BIGINT          NOT NULL COMMENT '内容ID',
    like_count    BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    dislike_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点踩数',
    collect_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
    comment_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
    PRIMARY KEY (content_type, content_id)
);

-- 互动表（合并点赞/点踩）
CREATE TABLE `interaction`
(
    `interaction_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '互动ID',
    `user_id`        BIGINT                            NOT NULL COMMENT '用户ID',
    `content_type`   VARCHAR(20)                       NOT NULL COMMENT '内容类型',
    `content_id`     BIGINT                            NOT NULL COMMENT '内容ID',
    `action_type`    ENUM ('like','dislike','collect') NOT NULL COMMENT '行为类型',
    `created_at`     DATETIME(3)                       NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',

    -- 唯一约束：用户对同一内容只能有一种互动
    UNIQUE INDEX udx_user_content (`user_id`, `content_type`, `content_id`),

    -- 查询优化索引
    INDEX idx_content_stats (`content_type`, `content_id`, `action_type`),
    INDEX idx_user_actions (`user_id`, `action_type`, `created_at`),

    FOREIGN KEY (`content_type`) REFERENCES `content_type` (`type_code`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 收藏夹系统
CREATE TABLE `favorite_folder`
(
    `folder_id`  BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id`    BIGINT                              NOT NULL,
    `name`       VARCHAR(50)                         NOT NULL,
    `cover`      VARCHAR(512) COMMENT '封面URL',
    `sort_seq`   INT UNSIGNED                        NOT NULL COMMENT '排序序列值（程序维护）',
    `visibility` ENUM ('private','public','partial') NOT NULL DEFAULT 'private',
    `created_at` DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    INDEX idx_user_folders (`user_id`, `sort_seq`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 收藏项
CREATE TABLE `favorite_item`
(
    `item_id`      BIGINT PRIMARY KEY AUTO_INCREMENT,
    `folder_id`    BIGINT       NOT NULL,
    `content_type` VARCHAR(20)  NOT NULL,
    `content_id`   BIGINT       NOT NULL,
    `notes`        VARCHAR(200) NOT NULL DEFAULT '',
    `created_at`   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    -- 唯一约束防重复收藏
    UNIQUE INDEX udx_folder_content (`folder_id`, `content_type`, `content_id`),

    -- 查询优化
    INDEX idx_content_favorites (`content_type`, `content_id`, `folder_id`),

    FOREIGN KEY (`folder_id`) REFERENCES `favorite_folder` (`folder_id`) ON DELETE CASCADE,
    FOREIGN KEY (`content_type`) REFERENCES `content_type` (`type_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 收藏夹可见范围表（支持partial可见性）
CREATE TABLE `folder_visibility`
(
    `folder_id`  BIGINT      NOT NULL,
    `user_id`    BIGINT      NOT NULL COMMENT '允许查看的用户',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (`folder_id`, `user_id`),
    FOREIGN KEY (`folder_id`) REFERENCES `favorite_folder` (`folder_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- 弹幕模块设计
-- ----------------------------

-- 弹幕类型配置表
CREATE TABLE `danmaku_type`
(
    `type_id`      TINYINT UNSIGNED PRIMARY KEY COMMENT '弹幕类型ID',
    `type_name`    VARCHAR(20)  NOT NULL COMMENT '类型名称',
    `css_template` VARCHAR(100) NOT NULL COMMENT '样式模板',
    UNIQUE INDEX idx_type_name (`type_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 预置弹幕类型
INSERT INTO danmaku_type (type_id, type_name, css_template)
VALUES (1, '滚动弹幕', 'animation: scroll {duration}s linear;'),
       (2, '顶部弹幕', 'position: fixed; top: 10%;'),
       (3, '底部弹幕', 'position: fixed; bottom: 10%;');

-- 弹幕主表
CREATE TABLE `danmaku`
(
    `danmaku_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '弹幕ID',
    `video_id`   BIGINT                 NOT NULL COMMENT '视频ID',
    `user_id`    BIGINT                 NOT NULL COMMENT '用户ID',
    `content`    VARCHAR(100)           NOT NULL COMMENT '弹幕内容',
    `color`      CHAR(7)                NOT NULL DEFAULT '#FFFFFF' COMMENT '颜色值',
    `type_id`    TINYINT UNSIGNED       NOT NULL COMMENT '弹幕类型',
    `font_size`  TINYINT UNSIGNED       NOT NULL DEFAULT 20 COMMENT '字体大小(px)',
    `timepoint`  DECIMAL(6, 2) UNSIGNED NOT NULL COMMENT '出现时间(秒)',
    `duration`   DECIMAL(4, 1) UNSIGNED NOT NULL COMMENT '持续时间(秒)',
    `weight`     SMALLINT               NOT NULL DEFAULT 0 COMMENT '权重（排序用）',
    `state`      TINYINT                NOT NULL DEFAULT 0 COMMENT '状态(0-待审核 1-已通过 2-已屏蔽)',
    `created_at` DATETIME(3)            NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    -- 索引优化
    INDEX idx_video_time (`video_id`, `timepoint`, `state`),
    INDEX idx_user_danmaku (`user_id`, `created_at`),

    -- 外键约束
    FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`type_id`) REFERENCES `danmaku_type` (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 弹幕实时缓存表（处理高并发）
CREATE TABLE `danmaku_cache`
(
    `video_id`  BIGINT            NOT NULL COMMENT '视频ID',
    `timepoint` SMALLINT UNSIGNED NOT NULL COMMENT '时间片（秒/10）',
    `data`      JSON              NOT NULL COMMENT '压缩后的弹幕数据',
    PRIMARY KEY (`video_id`, `timepoint`),
    INDEX idx_video_time_window (`video_id`, `timepoint`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 弹幕屏蔽规则表
CREATE TABLE `danmaku_filter`
(
    `filter_id`    INT PRIMARY KEY AUTO_INCREMENT,
    `pattern_type` ENUM ('keyword','regex','user')   NOT NULL,
    `pattern`      VARCHAR(100)                      NOT NULL COMMENT '匹配规则',
    `action`       ENUM ('block','replace','review') NOT NULL DEFAULT 'block',
    `replacement`  VARCHAR(50)                                DEFAULT NULL COMMENT '替换内容',
    `valid_from`   DATETIME                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `valid_to`     DATETIME                                   DEFAULT NULL,
    INDEX idx_active_filters (`valid_from`, `valid_to`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 弹幕操作日志表
CREATE TABLE `danmaku_log`
(
    `log_id`      BIGINT PRIMARY KEY AUTO_INCREMENT,
    `danmaku_id`  BIGINT                                  NOT NULL,
    `action`      ENUM ('send','delete','block','report') NOT NULL,
    `operator_id` BIGINT COMMENT '操作人ID',
    `detail`      JSON COMMENT '操作详情',
    `created_at`  DATETIME(3)                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    INDEX idx_danmaku_actions (`danmaku_id`, `action`),
    FOREIGN KEY (`danmaku_id`) REFERENCES `danmaku` (`danmaku_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- 分类相关
-- ----------------------------
-- 分类表优化：使用ENUM类型并调整外键注释
CREATE TABLE `category`
(
    `category_id`   BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `category_type` ENUM ('product','article' 'video') NOT NULL COMMENT '分类体系类型', -- 改为ENUM
    `name`          VARCHAR(50)                        NOT NULL COMMENT '分类名称',
    `slug`          VARCHAR(50)                        NOT NULL COMMENT 'URL友好标识',
    `parent_id`     BIGINT UNSIGNED                             DEFAULT NULL COMMENT '父分类ID',
    `icon`          VARCHAR(255) COMMENT '分类图标',
    `sort_order`    SMALLINT                           NOT NULL DEFAULT 32767 COMMENT '排序值（小值靠前）',
    `is_active`     TINYINT(1)                         NOT NULL DEFAULT 1 COMMENT '启用状态',
    `created_at`    DATETIME(3)                        NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `updated_at`    DATETIME(3)                        NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    -- 唯一约束：同一父类下slug唯一
    UNIQUE INDEX `udx_parent_slug` (`parent_id`, `category_type`, `slug`),

    -- 快速查询直接子节点
    INDEX `idx_parent` (`parent_id`),

    -- 排序索引
    INDEX `idx_sort_order` (`category_type`, `sort_order`),

    FOREIGN KEY (`parent_id`) REFERENCES `category` (`category_id`)
        ON DELETE SET NULL                                                              -- 根据业务需求确认
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 标签表优化：添加外键约束和索引
CREATE TABLE `tag`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    `name`         VARCHAR(50) NOT NULL COLLATE utf8mb4_bin COMMENT '标签名称',
    `normalized`   VARCHAR(50) GENERATED ALWAYS AS (LOWER(TRIM(`name`))) STORED UNIQUE COMMENT '标准化标签',
    `parent_id`    BIGINT COMMENT '支持标签层级',
    `synonym_id`   BIGINT COMMENT '主标签ID',
    `search_count` BIGINT      NOT NULL DEFAULT 0 COMMENT '搜索权重',
    `heat_score`   FLOAT       NOT NULL DEFAULT 0.0 COMMENT '热度评分',
    `created_by`   BIGINT      NOT NULL COMMENT '创建者ID',
    `created_at`   DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',

    -- 外键约束确保父标签和同义词有效
    FOREIGN KEY (`parent_id`) REFERENCES `tag` (`id`)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (`synonym_id`) REFERENCES `tag` (`id`)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    -- 索引优化
    INDEX `idx_tag_parent` (`parent_id`),
    INDEX `idx_synonym` (`synonym_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 内容标签关联表优化：添加外键约束
CREATE TABLE `content_tag`
(
    `content_type` ENUM ('video','article','product') NOT NULL COMMENT '内容类型',
    `content_id`   BIGINT                             NOT NULL COMMENT '内容ID',
    `tag_id`       BIGINT                             NOT NULL COMMENT '标签ID',
    `weight`       DECIMAL(5, 4)                      NOT NULL DEFAULT 1.0000 COMMENT '权重',
    `source`       ENUM ('system','user','ai')        NOT NULL DEFAULT 'system' COMMENT '来源',
    `created_at`   DATETIME(3)                        NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    PRIMARY KEY (`content_type`, `content_id`, `tag_id`),

    -- 外键约束确保标签存在
    FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- 可选：按tag_id查询的索引
    INDEX `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- 视频相关
-- ----------------------------
-- 视频表
CREATE TABLE `video`
(
    `video_id`    BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '视频ID',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    `title`       VARCHAR(255)    NOT NULL COMMENT '视频标题',
    `description` TEXT            NOT NULL COMMENT '描述',
    `cover_url`   VARCHAR(255)    NOT NULL COMMENT '封面地址',
    `duration`    TIME(3)         NOT NULL COMMENT '时长（含毫秒）',
    `status`      ENUM ('pending','approved','rejected','deleted')
                                  NOT NULL DEFAULT 'pending' COMMENT '状态: pending-审核中, approved-通过, rejected-未通过, deleted-删除',
    `category_id` BIGINT UNSIGNED NOT NULL COMMENT '分类ID',
    `visibility`  ENUM ('private','public')
                                  NOT NULL DEFAULT 'public' COMMENT '可见性: private-私密, public-公开',
    `created_at`  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `updated_at`  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    -- 外键约束
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- 分类必须属于video类型（需确保category_type='video'）
    FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    -- 索引优化
    INDEX `idx_user_status` (`user_id`, `status`),
    INDEX `idx_category` (`category_id`),
    INDEX `idx_visibility` (`visibility`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 视频清晰度表
CREATE TABLE `video_quality`
(
    `quality_id`   BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '清晰度ID',
    `video_id`     BIGINT UNSIGNED NOT NULL COMMENT '视频ID',
    `quality_type` VARCHAR(20)     NOT NULL COMMENT '清晰度类型（关联quality_type表）',
    `video_url`    VARCHAR(255)    NOT NULL COMMENT '视频地址',
    `bitrate`      INT UNSIGNED    NOT NULL COMMENT '码率(kbps)',
    `created_at`   DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `updated_at`   DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    -- 唯一约束防止重复清晰度
    UNIQUE INDEX `udx_video_quality` (`video_id`, `quality_type`),

    -- 外键约束
    FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- 索引优化
    INDEX `idx_quality_type` (`quality_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 清晰度类型字典表
CREATE TABLE `quality_type`
(
    `type_code`  VARCHAR(20) PRIMARY KEY COMMENT '清晰度类型编码',
    `type_name`  VARCHAR(50) NOT NULL COMMENT '显示名称',
    `resolution` VARCHAR(20) NOT NULL COMMENT '分辨率（如1920x1080）',
    `sort_order` SMALLINT    NOT NULL DEFAULT 32767 COMMENT '排序值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 视频统计表（计数分离）
CREATE TABLE `video_stats`
(
    `video_id`       BIGINT UNSIGNED PRIMARY KEY COMMENT '视频ID',
    `view_count`     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '播放数',
    `like_count`     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    `favorite_count` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
    `comment_count`  BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
    `share_count`    BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '分享数',
    `updated_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',

    -- 外键约束
    FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- 文章相关
-- ----------------------------
-- 文章基础表
CREATE TABLE `article`
(
    `article_id`     BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',
    `user_id`        BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    `category_id`    BIGINT UNSIGNED NOT NULL COMMENT '分类ID',
    `title`          VARCHAR(255)    NOT NULL COMMENT '标题',
    `slug`           VARCHAR(255)    NOT NULL COMMENT 'URL友好标识',
    `excerpt`        VARCHAR(500)    NOT NULL COMMENT '摘要',
    `content`        LONGTEXT        NOT NULL COMMENT '内容（支持富文本）',
    `cover_url`      VARCHAR(255)             DEFAULT NULL COMMENT '封面图地址',
    `status`         ENUM ('draft','pending','published','archived','deleted')
                                     NOT NULL DEFAULT 'draft' COMMENT '状态',
    `visibility`     ENUM ('public','private','password_protected','scheduled')
                                     NOT NULL DEFAULT 'public' COMMENT '可见性',
    `comment_status` BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否允许评论',
    `published_at`   DATETIME(3)              DEFAULT NULL COMMENT '发布时间',
    `created_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    UNIQUE KEY `uniq_slug` (`slug`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
    INDEX `idx_status_published` (`status`, `published_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 可见性设置扩展表
CREATE TABLE `visibility_settings`
(
    `article_id` BIGINT UNSIGNED PRIMARY KEY,
    `password`   VARCHAR(100) DEFAULT NULL COMMENT '访问密码',
    `start_time` DATETIME     DEFAULT NULL COMMENT '可见开始时间',
    `end_time`   DATETIME     DEFAULT NULL COMMENT '可见结束时间',
    FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 合并后的媒体资源表（支持文章、动态等多种内容类型）
CREATE TABLE `media`
(
    `media_id`     BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '媒体资源ID',
    -- 通用内容关联字段 --
    `content_type` ENUM ('article', 'feed')              NOT NULL COMMENT '关联内容类型: article=文章, feed=动态',
    `content_id`   BIGINT UNSIGNED                       NOT NULL COMMENT '关联内容ID（如article_id或feed_id）',

    -- 公共字段 --
    `url`          VARCHAR(512)                          NOT NULL COMMENT '资源地址',
    `media_type`   ENUM ('image','video','audio','file') NOT NULL COMMENT '媒体类型',
    `description`  VARCHAR(255)                                   DEFAULT NULL COMMENT '描述文字',

    -- 动态媒体专属字段（允许NULL）--
    `width`        SMALLINT UNSIGNED                              DEFAULT NULL COMMENT '媒体宽度（像素，仅image/video有效）',
    `height`       SMALLINT UNSIGNED                              DEFAULT NULL COMMENT '媒体高度（像素，仅image/video有效）',
    `duration`     INT UNSIGNED                                   DEFAULT NULL COMMENT '音视频时长（秒，仅video/audio有效）',
    `sort_order`   TINYINT UNSIGNED                      NOT NULL DEFAULT 0 COMMENT '排序序号',

    -- 索引与约束 --
    INDEX `idx_content` (`content_type`, `content_id`),
    INDEX `idx_media_type` (`media_type`),
    INDEX `idx_sort` (`content_type`, `content_id`, `sort_order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 版本历史表
CREATE TABLE `version_history`
(
    `version_id`   BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    `article_id`   BIGINT UNSIGNED NOT NULL,
    `user_id`      BIGINT UNSIGNED NOT NULL,
    `content_diff` LONGTEXT        NOT NULL COMMENT '内容差异',
    `created_at`   DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 文章统计表
CREATE TABLE `statistics`
(
    `article_id` BIGINT UNSIGNED PRIMARY KEY,
    `views`      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读量',
    `likes`      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    `comments`   INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
    `shares`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '分享数',
    FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- 动态相关
-- ----------------------------
CREATE TABLE `user_feed`
(
    `feed_id`        BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '动态ID',
    `user_id`        BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',
    `content`        TEXT            NOT NULL COMMENT '动态内容（支持Markdown/HTML）',
    `feed_type`      ENUM ('post','image','video','share','article')
                                     NOT NULL DEFAULT 'post' COMMENT '动态类型',
    `visibility`     ENUM ('public','friends','private','selected')
                                     NOT NULL DEFAULT 'public' COMMENT '可见范围',
    `parent_feed_id` BIGINT UNSIGNED          DEFAULT NULL COMMENT '转发源动态ID',
    `location`       POINT SRID 4326 COMMENT '地理坐标（WGS84）',
    `status`         ENUM ('active','deleted','archived','pending')
                                     NOT NULL DEFAULT 'active' COMMENT '状态',
    `created_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    -- 空间索引
    SPATIAL INDEX `idx_location` (`location`),
    -- 外键约束
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (`parent_feed_id`) REFERENCES `user_feed` (`feed_id`)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    -- 组合索引
    INDEX `idx_user_visibility` (`user_id`, `visibility`, `created_at`),
    INDEX `idx_type_status` (`feed_type`, `status`),
    INDEX `idx_timeline` (`created_at`, `feed_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 动态可见范围白名单（当visibility=selected时生效）
CREATE TABLE `feed_visibility_whitelist`
(
    `feed_id` BIGINT UNSIGNED NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`feed_id`, `user_id`),
    FOREIGN KEY (`feed_id`) REFERENCES `user_feed` (`feed_id`)
        ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- 动态互动统计表
CREATE TABLE `feed_stats`
(
    `feed_id`       BIGINT UNSIGNED PRIMARY KEY,
    `like_count`    INT UNSIGNED NOT NULL DEFAULT 0,
    `comment_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `share_count`   INT UNSIGNED NOT NULL DEFAULT 0,
    `view_count`    INT UNSIGNED NOT NULL DEFAULT 0,
    `hot_score`     FLOAT        NOT NULL DEFAULT 0 COMMENT '热度分数',

    FOREIGN KEY (`feed_id`) REFERENCES `user_feed` (`feed_id`)
        ON DELETE CASCADE,
    INDEX `idx_hot_score` (`hot_score` DESC)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- 社交功能表结构优化（含索引与分区建议）
-- ----------------------------

-- 会话基础表（单聊/群聊通用）
CREATE TABLE `conversation`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `type`         TINYINT      NOT NULL DEFAULT 1 COMMENT '1=单聊 2=群聊',
    `identifier`   VARCHAR(64)  NOT NULL UNIQUE COMMENT '会话唯一标识（单聊哈希值=MD5(sorted_user_ids)）',
    `custom_name`  VARCHAR(100) COMMENT '用户自定义名称',
    `default_name` VARCHAR(100) COMMENT '系统生成名称（自动填充规则）',
    `avatar`       VARCHAR(255) NOT NULL DEFAULT 'https://cdn.default.com/avatar.png',
    `created_at`   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `archived`     BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否归档会话',
    `version`      INT          NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 会话参与者表
CREATE TABLE `participant`
(
    `conversation_id` BIGINT      NOT NULL COMMENT '会话ID',
    `user_id`         BIGINT      NOT NULL COMMENT '用户ID',
    `role`            TINYINT     NOT NULL DEFAULT 1 COMMENT '1=成员 2=管理员 3=超级管理员',
    `joined_at`       DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `muted_expire`    DATETIME(3) COMMENT '禁言过期时间（NULL表示未禁言）',
    `last_read_at`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (`conversation_id`, `user_id`),
    FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 消息表（分区方案建议）
CREATE TABLE `message`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    `conversation_id` BIGINT      NOT NULL COMMENT '会话ID',
    `sender_id`       BIGINT      NOT NULL COMMENT '发送者ID',
    `client_msg_id`   VARCHAR(64) NOT NULL COMMENT '客户端幂等ID（UUID）',
    `content_type`    TINYINT     NOT NULL DEFAULT 1 COMMENT '1=文本 2=图片 3=视频 4=文件 5=撤回 6=群公告',
    `content`         TEXT        NOT NULL COMMENT '加密内容/资源URI',
    `metadata`        JSON COMMENT '扩展元数据（文件信息/@消息等）',
    `status`          TINYINT     NOT NULL DEFAULT 1 COMMENT '1=发送中 2=已发送 3=已送达 4=部分读 5=全部读',
    `deleted`         BOOLEAN     NOT NULL DEFAULT FALSE,
    `created_at`      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 消息已读状态表
CREATE TABLE `message_read`
(
    `message_id` BIGINT      NOT NULL COMMENT '消息ID',
    `user_id`    BIGINT      NOT NULL COMMENT '用户ID',
    `read_at`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '阅读时间',
    `device_id`  VARCHAR(64) COMMENT '阅读设备标识' COMMENT '设备标识（UUID）',
    PRIMARY KEY (`message_id`, `user_id`),
    FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 群组扩展表
CREATE TABLE `group`
(
    `conversation_id`  BIGINT PRIMARY KEY COMMENT '会话ID',
    `max_members`      INT         NOT NULL DEFAULT 1000 COMMENT '最大成员数',
    `announcement`     TEXT COMMENT '当前公告（Markdown格式）',
    `announce_version` INT         NOT NULL DEFAULT 0 COMMENT '公告版本号',
    `permission_level` TINYINT     NOT NULL DEFAULT 2 COMMENT '1=全员管理 2=仅管理员 3=仅群主',
    `invite_mode`      TINYINT     NOT NULL DEFAULT 1 COMMENT '1=自由邀请 2=需审核 3=禁止邀请',
    `join_mode`        TINYINT     NOT NULL DEFAULT 1 COMMENT '1=自由加入 2=需审核 3=禁止加入',
    `history_visible`  BOOLEAN     NOT NULL DEFAULT TRUE COMMENT '新成员查看历史消息',
    `created_by`       BIGINT      NOT NULL COMMENT '群主ID',
    `updated_at`       DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 群组历史公告表
CREATE TABLE `group_announcement`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    `conversation_id` BIGINT      NOT NULL COMMENT '会话ID',
    `content`         TEXT        NOT NULL COMMENT '公告内容（Markdown格式）',
    `publisher_id`    BIGINT      NOT NULL COMMENT '发布者ID',
    `publish_time`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `version`         INT         NOT NULL COMMENT '关联group表的announce_version',
    FOREIGN KEY (`conversation_id`) REFERENCES `group` (`conversation_id`) ON DELETE CASCADE,
    FOREIGN KEY (`publisher_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
-- ----------------------------
-- 其他功能表结构优化（含索引）
-- ---------------------------
-- 历史记录优化（支持多类型）
CREATE TABLE `watch_history`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '历史记录ID',
    `user_id`      BIGINT                        NOT NULL COMMENT '用户ID',
    `content_type` ENUM ('video','live','short') NOT NULL COMMENT '内容类型',
    `content_id`   BIGINT                        NOT NULL COMMENT '内容ID',
    `progress`     INT                           NOT NULL DEFAULT 0 COMMENT '毫秒精度' COMMENT '观看进度',
    `duration`     INT                           NOT NULL COMMENT '毫秒精度',
    `watch_count`  INT                           NOT NULL DEFAULT 1 COMMENT '观看次数',
    `last_watched` DATETIME(3)                   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '最后观看时间',
    `device_id`    CHAR(16)                      NOT NULL COMMENT '设备指纹摘要',
    `context`      JSON COMMENT '观看上下文信息'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 稍后再看优化（自动排序）
CREATE TABLE `watch_later`
(
    `user_id`   BIGINT      NOT NULL COMMENT '用户ID',
    `video_id`  BIGINT      NOT NULL COMMENT '视频ID',
    `priority`  TINYINT     NOT NULL DEFAULT 2 COMMENT '0=低 1=中 2=高',
    `added_at`  DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '添加时间',
    `scheduled` DATETIME(3) COMMENT '计划观看时间',
    PRIMARY KEY (`user_id`, `video_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 增强预约系统
CREATE TABLE `reminder`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '提醒ID',
    `user_id`        BIGINT                                      NOT NULL COMMENT '用户ID',
    `event_type`     ENUM ('live','premiere','product','custom') NOT NULL COMMENT '事件类型',
    `event_id`       BIGINT                                      NOT NULL COMMENT '关联事件ID',
    `notify_channel` SET ('push','sms','email','calendar')       NOT NULL DEFAULT 'push' COMMENT '通知渠道',
    `status`         ENUM ('pending','triggered','canceled')     NOT NULL DEFAULT 'pending' COMMENT '状态',
    `scheduled_time` DATETIME(3)                                 NOT NULL COMMENT '提醒时间',
    `timezone`       VARCHAR(64)                                 NOT NULL DEFAULT 'UTC' COMMENT '时区',
    `created_at`     DATETIME(3)                                 NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;