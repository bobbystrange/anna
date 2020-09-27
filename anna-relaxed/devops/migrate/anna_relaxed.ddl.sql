create database if not exists `anna_relaxed`;

create table if not exists `table_def` (
    `id`            bigint unsigned not null auto_increment,
    `tenant_id`     bigint unsigned not null,
    `create_time`   timestamp       not null default current_timestamp,
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp,
    `name`          varchar(100)    not null,
    `display_name`  varchar(100)    not null,
    `source_table`  varchar(100)    not null,
    `source_column` varchar(100)    not null,
    primary key (`id`),
    unique key `idx_tenant_id_name` (`tenant_id`, `name`)
) engine = InnoDB
  default charset = utf8mb4;

create table if not exists `column_def` (
    `id`           bigint unsigned not null auto_increment,
    `tenant_id`    bigint unsigned not null,
    `create_time`  timestamp       not null default current_timestamp,
    `update_time`  timestamp       not null default current_timestamp on update current_timestamp,
    `table_id`     bigint unsigned not null,
    `name`         varchar(100)    not null,
    `display_name` varchar(100)    not null,
    `type`         smallint        not null,
    `source`       varchar(511)    null,
    `flag`         smallint        not null default 0,
    primary key (`id`),
    unique key `idx_table_id_name` (`table_id`, `name`)
) engine = InnoDB
  default charset = utf8mb4;

create table if not exists `row_data` (
    `id`            bigint unsigned not null auto_increment,
    `tenant_id`     bigint unsigned not null,
    `create_time`   timestamp       not null default current_timestamp,
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp,
    `table_id`      bigint unsigned not null,
    `primary_value` varchar(255)    not null,
    `column_id`     bigint unsigned not null,
    `value`         varchar(255)    not null,
    primary key (`id`),
    unique key `idx_primary_value_column_id` (`primary_value`, `column_id`)
) engine = InnoDB
  default charset = utf8mb4;

create table if not exists `select_value_strategy` (
    `id`          bigint unsigned not null auto_increment,
    `tenant_id`   bigint unsigned not null,
    `create_time` timestamp       not null default current_timestamp,
    `update_time` timestamp       not null default current_timestamp on update current_timestamp,
    `table_id`    bigint unsigned not null,
    `column_id`   bigint unsigned not null,
    `value`       varchar(255)    not null,
    primary key (`id`),
    key `idx_column_id` (`column_id`)
) engine = InnoDB
  default charset = utf8mb4;
