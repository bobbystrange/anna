create database if not exists `anna`;
use `anna`;

create table if not exists `table_def`
(
    `id`            bigint      not null auto_increment comment '主键ID',
    `tenant_id`     char(36)    not null comment '租户ID',
    `created_time`  timestamp   not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '创建时间',
    `updated_time`  timestamp   not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '修改时间',
    `created_by`    bigint      not null comment '创建人',
    `updated_by`    bigint      not null comment '修改人',
    `name`          varchar(30) not null comment '名称',
    `domain`        varchar(30) null comment '领域编码',
    `business_type` tinyint              default 1 comment '业务类型(1.基础资料类型，2.单据类型)',
    `status`        tinyint     not null default 1 comment '状态，1启用，2禁用',
    primary key (`id`),
    key (`api_name`)
) comment '实体表';
