create table IF NOT EXISTS authority.`user`
(
    `id` bigint(20) unsigned auto_increment comment '主键ID',
    `username` varchar(50) not null comment '账号',
    `password` varchar(50) not null comment '密码',
    `remark_name` varchar(50) not null comment '备注名',
    `is_manager` tinyint default 0 comment '是否为管理员：0-普通用户、1-管理员',
    `status` tinyint default 0 comment '状态（0：待审批，1：使用中，2:已禁用）',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_username`(`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户信息表';


create table IF NOT EXISTS authority.`resource`
(
    `id` bigint(20) unsigned auto_increment comment '自增ID',
    `resource_id` varchar(200) not null comment '资源ID',
    `resource_name` varchar(50) not null comment '资源名称',
    `system_id` varchar(50) not null comment '系统ID',
    `resource_detail` json  comment '资源详情',
    `resource_type` tinyint comment '资源类型',
    `description` varchar(50) comment '资源描述',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_resource_system`(`resource_id`,`system_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '资源信息表';

    create table IF NOT EXISTS authority.`system`
(
    `id` bigint(20) unsigned auto_increment comment '自增ID',
    `system_id` varchar(100) not null comment '系统名称',
    `description` varchar(200) comment '功能描述',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_system_id`(`system_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '系统信息表';


	create table IF NOT EXISTS authority.`role`
(
    `id` bigint(20) unsigned auto_increment comment '自增ID',
    `role_name` varchar(50) not null comment '角色名称',
    `system_id` varchar(50) not null comment '归属系统',
    `description` varchar(100) comment '角色描述',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_role_message`(`role_name`,`system_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '角色信息表';

    create table IF NOT EXISTS authority.`user_role`
(
    `id` bigint(20) unsigned auto_increment comment '主键ID',
    `username` varchar(50) not null comment '账号',
    `role_name` varchar(50) not null comment '角色名称',
    `system_id` varchar(50) not null comment '归属系统',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_message`(`username`,`system_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户角色信息表';


create table IF NOT EXISTS authority.`logs`
(
    `id` bigint(20) unsigned auto_increment comment '主键ID',
    `operator` varchar(50) not null comment '操作者',
    `operation` tinyint default 0 comment '操作种类：0插入，1更新，2删除',
    `record` text not null comment 'log详细内容',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment 'log记录表';

    create table IF NOT EXISTS authority.`role_resource`
(
    `id` bigint(20) unsigned auto_increment comment '自增ID',
    `role_name` varchar(50) not null comment '角色名称',
    `system_id` varchar(50) not null comment '归属系统',
    `resource_id` varchar(50) not null comment '资源ID',
    `create_time` datetime default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key `pk_id`(`id`),
    unique key `uk_role_resource`(`role_name`,`system_id`,`resource_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '角色资源信息表';