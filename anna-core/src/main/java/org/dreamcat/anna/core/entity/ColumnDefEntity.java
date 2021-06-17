package org.dreamcat.anna.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by tuke on 2020/9/11
 */
@Getter
@Setter
@Table
@Entity(name = "column_def")
public class ColumnDefEntity extends BaseEntity {

    @Column(name = "table_id")
    private Long tableId;
    private String name;
    private String alias;
    private Boolean nullable;
    private Boolean partial;
}
