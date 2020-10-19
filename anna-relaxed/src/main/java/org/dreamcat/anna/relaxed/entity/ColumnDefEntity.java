package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;
import org.dreamcat.anna.relaxed.core.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    @Column(name = "display_name")
    private String displayName;
    private ColumnType type;
    private Boolean primary;
    private Boolean required;
    private Boolean unique;
}
