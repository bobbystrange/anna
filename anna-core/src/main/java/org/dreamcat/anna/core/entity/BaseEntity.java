package org.dreamcat.anna.core.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by tuke on 2020/9/11
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    // bigint unsigned not null auto_increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // bigint unsigned not null
    @Column(name = "tenant_id")
    private Long tenantId;
    // not null default current_timestamp
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;
    // not null default current_timestamp on update current_timestamp
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
    // bigint unsigned default null
    @Column(name = "created_by")
    private Long createdBy;
    // bigint unsigned default null
    @Column(name = "updated_by")
    private Long updatedBy;
}
