package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Create by tuke on 2020/9/11
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    // bigint unsigned     not null auto_increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    // not null default current_timestamp
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "create_time", insertable = false)
    private Date createTime;
    // not null default current_timestamp on update current_timestamp
    @Column(name = "update_time", insertable = false)
    private Date updateTime;
}
