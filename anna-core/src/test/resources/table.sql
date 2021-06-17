create table t (
    a bigint not null auto_increment comment '',
    b timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '',
    c varchar(36) null comment '',
    d decimal(16, 6),
    primary key (a),
    unique key (b, c),
    key (d)
) comment '';
