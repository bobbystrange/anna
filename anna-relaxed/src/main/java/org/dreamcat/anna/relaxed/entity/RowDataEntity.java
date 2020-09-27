package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Create by tuke on 2020/9/12
 */
@Getter
@Setter
@Table
@Entity(name = "row_data")
public class RowDataEntity extends BaseEntity {
    @Column(name = "table_id")
    private Long tableId;
    @Column(name = "column_id")
    private Long columnId;
    /**
     * the value of primary key
     */
    @Column(name = "primary_value")
    private String primaryValue;
    /**
     * null in primary column, and column value in other column
     */
    private String value;
}
