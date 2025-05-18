-- 用户管理模块权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('user:create', '创建用户', '允许创建新的用户账户', 'user'),
       ('user:read', '查看用户', '允许查看用户详细信息', 'user'),
       ('user:update', '编辑用户', '允许修改用户资料', 'user'),
       ('user:delete', '删除用户', '允许移除用户账户', 'user'),
       ('user:list', '用户列表', '允许浏览所有用户列表', 'user');

-- 角色管理模块权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('role:create', '创建角色', '允许定义新的系统角色', 'role'),
       ('role:read', '查看角色', '允许查看角色权限配置', 'role'),
       ('role:update', '编辑角色', '允许修改角色信息', 'role'),
       ('role:delete', '删除角色', '允许移除现有角色', 'role'),
       ('role:assign', '分配角色', '允许为用户分配角色', 'role');

-- 权限管理模块权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('perm:grant', '授权权限', '允许为角色分配权限', 'permission'),
       ('perm:revoke', '撤销权限', '允许从角色移除权限', 'permission'),
       ('perm:audit', '权限审计', '允许查看权限分配记录', 'permission');

-- 内容管理模块权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('article:publish', '发布文章', '允许创建并发布新内容', 'content'),
       ('article:edit', '编辑文章', '允许修改已发布内容', 'content'),
       ('article:review', '内容审核', '允许审核待发布内容', 'content'),
       ('article:archive', '归档文章', '允许下架历史内容', 'content');

-- 订单管理模块权限（添加查看权限）
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('order:create', '新建订单', '允许生成销售订单', 'order'),
       ('order:process', '处理订单', '允许执行订单流程操作', 'order'),
       ('order:cancel', '取消订单', '允许终止订单交易', 'order'),
       ('order:export', '导出订单', '允许批量导出订单数据', 'order'),
       ('order:read', '查看订单', '允许查看订单详细信息', 'order'), -- 新增权限
       ('order:list', '订单列表', '允许浏览所有订单列表', 'order');
-- 新增权限

-- 系统管理权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('system:config', '系统配置', '允许修改系统参数设置', 'system'),
       ('system:monitor', '服务监控', '允许查看服务器运行状态', 'system'),
       ('system:backup', '数据备份', '允许执行数据库备份操作', 'system'),
       ('log:view', '查看日志', '允许访问系统操作日志记录', 'system');

-- 财务相关权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('finance:reconcile', '对账操作', '允许执行资金对账流程', 'finance'),
       ('finance:report', '财务报表', '允许生成财务分析报告', 'finance'),
       ('payment:verify', '支付核验', '允许审核支付交易记录', 'finance');

-- 库存管理模块权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('inventory:view', '查看库存', '允许查看商品库存信息', 'inventory'),
       ('inventory:adjust', '库存调整', '允许修改库存数量', 'inventory'),
       ('inventory:transfer', '库存调拨', '执行跨仓库库存转移', 'inventory');

-- 报表与客服权限
INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('report:sales', '销售报表', '生成销售数据分析报表', 'report'),
       ('report:user', '用户报表', '生成用户行为分析报表', 'report'),
       ('ticket:handle', '处理工单', '处理用户提交的服务工单', 'customer_service'),
       ('ticket:assign', '分配工单', '将工单分配给客服人员', 'customer_service');

INSERT INTO permission (perm_code, perm_name, perm_desc, perm_group)
VALUES ('self:update', '维护资料', '允许修改自己的账户信息', 'user'),
       ('order:self:list', '我的订单', '查看本人的订单列表', 'order'),
       ('order:self:read', '订单详情', '查看本人订单详细信息', 'order'),
       ('ticket:create', '提交工单', '创建新的服务请求工单', 'customer_service'),
       ('article:read', '阅读文章', '查看已发布的内容详情', 'content'),
       ('password:change', '修改密码', '定期更新账户密码', 'security'),
       ('notification:view', '消息通知', '查看系统和个人消息', 'user');

-- 系统核心角色
INSERT INTO role (role_code, role_name, role_desc, is_system)
VALUES ('super_admin', '超级管理员', '拥有系统全部权限，负责整体运维', 1),
       ('sys_operator', '系统运维员', '负责服务器监控和系统配置', 1),
       ('user_admin', '用户管理员', '管理用户账号和基础权限分配', 1),
       ('content_admin', '内容管理员', '负责审核和管理平台内容', 1),
       ('financial_clerk', '财务专员', '处理订单及财务相关操作', 1),
       ('basic_user', '普通用户', '基础注册用户权限', 1),
       ('hr_manager', 'HR经理', '人力资源管理专用角色', 0),
       ('sales_manager', '销售主管', '管理销售订单和客户数据', 0),
       ('tech_support', '技术支持', '查看系统日志和用户反馈', 0);


-- 超级管理员（拥有所有权限）
INSERT INTO role_permission (role_id, perm_id)
SELECT 1, perm_id
FROM permission;

-- 系统运维员（系统管理+监控）
INSERT INTO role_permission (role_id, perm_id)
VALUES (2, (SELECT perm_id FROM permission WHERE perm_code = 'system:config')),
       (2, (SELECT perm_id FROM permission WHERE perm_code = 'system:monitor')),
       (2, (SELECT perm_id FROM permission WHERE perm_code = 'system:backup')),
       (2, (SELECT perm_id FROM permission WHERE perm_code = 'log:view'));

-- 用户管理员（用户管理模块）
INSERT INTO role_permission (role_id, perm_id)
VALUES (3, (SELECT perm_id FROM permission WHERE perm_code = 'user:create')),
       (3, (SELECT perm_id FROM permission WHERE perm_code = 'user:read')),
       (3, (SELECT perm_id FROM permission WHERE perm_code = 'user:update')),
       (3, (SELECT perm_id FROM permission WHERE perm_code = 'user:list')),
       (3, (SELECT perm_id FROM permission WHERE perm_code = 'role:assign'));

-- 内容管理员（内容管理+日志查看）
INSERT INTO role_permission (role_id, perm_id)
VALUES (4, (SELECT perm_id FROM permission WHERE perm_code = 'article:publish')),
       (4, (SELECT perm_id FROM permission WHERE perm_code = 'article:edit')),
       (4, (SELECT perm_id FROM permission WHERE perm_code = 'article:review')),
       (4, (SELECT perm_id FROM permission WHERE perm_code = 'log:view'));

-- 财务专员（订单+财务模块）
INSERT INTO role_permission (role_id, perm_id)
VALUES (5, (SELECT perm_id FROM permission WHERE perm_code = 'order:export')),
       (5, (SELECT perm_id FROM permission WHERE perm_code = 'finance:reconcile')),
       (5, (SELECT perm_id FROM permission WHERE perm_code = 'finance:report')),
       (5, (SELECT perm_id FROM permission WHERE perm_code = 'payment:verify'));

-- 普通用户（基础权限）
INSERT INTO role_permission (role_id, perm_id)
VALUES (6, (SELECT perm_id FROM permission WHERE perm_code = 'user:read')),
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'self:update')),     -- 资料维护
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'order:self:list')), -- 查看个人订单
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'order:self:read')), -- 订单详情
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'ticket:create')),   -- 提交服务请求
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'article:read')),    -- 阅读内容
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'password:change')), -- 密码安全
       (6, (SELECT perm_id FROM permission WHERE perm_code = 'notification:view')); -- 消息通知,

INSERT INTO role_permission (role_id, perm_id)
VALUES (7, (SELECT perm_id FROM permission WHERE perm_code = 'user:read')),
       (7, (SELECT perm_id FROM permission WHERE perm_code = 'user:list')),
       (7, (SELECT perm_id FROM permission WHERE perm_code = 'role:assign'));

-- 销售主管：订单查看/导出+销售报表
INSERT INTO role_permission (role_id, perm_id)
VALUES (8, (SELECT perm_id FROM permission WHERE perm_code = 'order:read')),
       (8, (SELECT perm_id FROM permission WHERE perm_code = 'order:list')),
       (8, (SELECT perm_id FROM permission WHERE perm_code = 'order:export')),
       (8, (SELECT perm_id FROM permission WHERE perm_code = 'report:sales'));

-- 技术支持：日志查看+工单处理
INSERT INTO role_permission (role_id, perm_id)
VALUES (9, (SELECT perm_id FROM permission WHERE perm_code = 'log:view')),
       (9, (SELECT perm_id FROM permission WHERE perm_code = 'ticket:handle'));

INSERT INTO role_permission (role_id, perm_id)
VALUES (4, (SELECT perm_id FROM permission WHERE perm_code = 'article:archive'));