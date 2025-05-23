-- ----------------------------
-- 核心功能（补充完善）
-- ----------------------------

-- 用户基本信息表（补充用户状态字段）
CREATE TABLE `users`
(
    `user_id`               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `nickname`              VARCHAR(50)                                         NOT NULL DEFAULT 'default_nickname' COMMENT '用户昵称',
    `avatar`                VARCHAR(255)                                        NOT NULL DEFAULT '/avatar/75622209a4794d5fb1060c4bd8ff18fa.jpg' COMMENT '用户头像',
    `background_image`      VARCHAR(255)                                        NOT NULL DEFAULT '/background/75622209a4794d5fb1060c4bd8ff18fa.jpg' COMMENT '用户背景图',
    `signature`             VARCHAR(255)                                        NOT NULL DEFAULT '这个家伙很懒，什么都没有留下' COMMENT '个性签名',
    `gender`                ENUM ('man', 'woman', 'unknown')                    NOT NULL DEFAULT 'unknown' COMMENT '性别',
    `birthday`              DATE                                                         DEFAULT NULL COMMENT '生日',
    `privacy`               ENUM ('public', 'private', 'friends')               NOT NULL DEFAULT 'public' COMMENT '隐私设置: public/private/friends',
    `created_time`          DATETIME                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`          DATETIME                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 新增用户状态字段
    `account_status`        ENUM ('active', 'inactive', 'suspended', 'deleted') NOT NULL DEFAULT 'active' COMMENT '账户状态: active/inactive/suspended/deleted',
    `login_attempts`        INT                                                          DEFAULT 0 COMMENT '登录尝试次数',
    `last_login_time`       DATETIME COMMENT '最后登录时间',
    `last_login_ip`         VARCHAR(45) COMMENT '最后登录IP',
    `password_changed_time` DATETIME COMMENT '密码最后修改时间',

    INDEX idx_nickname (`nickname`(20)) COMMENT '昵称前缀索引（兼顾存储与查询效率）',
    INDEX idx_created_time (`created_time`) COMMENT '注册时间排序索引',
    INDEX idx_gender_birthday (`birthday`, `gender`) COMMENT '性别+生日联合索引',
    INDEX idx_privacy_status (`privacy`) COMMENT '隐私状态+账户状态联合索引',
    INDEX idx_time_combination (`created_time`, `updated_time`) COMMENT '时间维度联合索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 用户验证信息表（补充账户锁定相关字段）
CREATE TABLE `user_auths`
(
    `auth_id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '验证信息ID',
    `user_id`                 BIGINT     NOT NULL COMMENT '用户ID',
    `enabled`                 TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    `account_non_expired`     TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账号是否未过期',
    `account_non_locked`      TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账号是否未锁定',
    `credentials_non_expired` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '凭证是否未过期',
    `locked_until`            DATETIME COMMENT '账户锁定截止时间（NULL表示未锁定）',
    `failed_login_attempts`   INT                 DEFAULT 0 COMMENT '失败登录尝试次数',
    `last_failed_login_time`  DATETIME COMMENT '最后失败登录时间',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    INDEX `index_user_auths` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 认证类型字典表（不变）
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

-- 认证方式表（补充密码相关字段）
CREATE TABLE `auth_method`
(
    `auth_id`            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '认证方式ID',
    `user_id`            BIGINT       NOT NULL COMMENT '用户ID',
    `auth_type`          VARCHAR(20)  NOT NULL COMMENT '认证类型',
    `auth_key`           VARCHAR(320) NOT NULL COMMENT '登录凭证',
    `auth_secret`        VARCHAR(512) COMMENT '加密凭证',
    `is_active`          TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用',
    `expired_time`       DATETIME COMMENT '凭证过期时间',
    `last_used_time`     DATETIME COMMENT '最后使用时间',
    `last_used_ip`       VARCHAR(45) COMMENT '最后使用IP',
    `created_time`       DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 新增密码相关字段（仅适用于密码认证类型）
    `password_hash`      VARCHAR(512) COMMENT '密码哈希值（仅适用于password类型）',
    `password_salt`      VARCHAR(128) COMMENT '密码盐值（仅适用于password类型）',
    `password_algorithm` VARCHAR(50) COMMENT '密码算法（如bcrypt, argon2等）',

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

-- 用户令牌表（补充会话管理字段）
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

    -- 新增会话管理字段
    `device_info`    VARCHAR(255) COMMENT '设备信息（如浏览器、操作系统等）',
    `ip_address`     VARCHAR(45) COMMENT 'IP地址',
    `user_agent`     VARCHAR(255) COMMENT '用户代理',

    UNIQUE INDEX idx_unique_active_token (`user_id`, `token_type`, `active_flag`),
    UNIQUE INDEX idx_token_hash (`token_hash`),
    INDEX idx_expired_time (`expired_time`),
    INDEX idx_user_status (`user_id`, `status`),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- ----------------------------
-- 新增表：登录尝试记录表
-- ----------------------------
CREATE TABLE `login_attempt`
(
    `attempt_id`     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '尝试ID',
    `user_id`        BIGINT      NOT NULL COMMENT '用户ID',
    `attempt_time`   DATETIME    NOT NULL COMMENT '尝试时间',
    `ip_address`     VARCHAR(45) NOT NULL COMMENT 'IP地址',
    `user_agent`     VARCHAR(255) COMMENT '用户代理',
    `device_info`    VARCHAR(255) COMMENT '设备信息',
    `success`        TINYINT(1)  NOT NULL COMMENT '是否成功（1成功，0失败）',
    `failure_reason` VARCHAR(255) COMMENT '失败原因（如密码错误、账户锁定等）',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- 新增表：密码重置令牌表
-- ----------------------------
CREATE TABLE `password_reset_token`
(
    `token_id`     BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '令牌ID',
    `user_id`      BIGINT                           NOT NULL COMMENT '用户ID',
    `token`        VARCHAR(512) COLLATE utf8mb4_bin NOT NULL COMMENT '令牌',
    `token_hash`   VARCHAR(64) GENERATED ALWAYS AS (UPPER(SHA2(token, 256))) STORED COMMENT '令牌哈希（全大写存储）',
    `expired_time` DATETIME                         NOT NULL COMMENT '过期时间',
    `created_time` DATETIME                         NOT NULL COMMENT '创建时间',
    `used`         TINYINT(1)                       NOT NULL DEFAULT 0 COMMENT '是否已使用',
    UNIQUE INDEX idx_unique_token (`token_hash`),
    INDEX idx_user_token (`user_id`, `expired_time`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- ----------------------------
-- 新增表：账户锁定记录表
-- ----------------------------
CREATE TABLE `account_lock`
(
    `lock_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '锁定ID',
    `user_id`       BIGINT       NOT NULL COMMENT '用户ID',
    `lock_time`     DATETIME     NOT NULL COMMENT '锁定时间',
    `unlock_time`   DATETIME COMMENT '解锁时间（NULL表示未解锁）',
    `lock_reason`   VARCHAR(255) NOT NULL COMMENT '锁定原因',
    `lock_duration` INT COMMENT '锁定持续时间（秒，NULL表示永久锁定）',
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;