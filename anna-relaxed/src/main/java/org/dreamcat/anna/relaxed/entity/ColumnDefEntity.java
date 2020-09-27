package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;
import org.dreamcat.anna.relaxed.entity.strategy.ColumnType;

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
    // only works with source filed
    private String source;
    // <primary> <required> <unique>
    private Integer flag;

    // the related column on the source table
    public boolean isPrimary() {
        return (flag & 0b0100) != 0;
    }

    // only works with custom-defined type
    public boolean isRequired() {
        return (flag & 0b0010) != 0;
    }

    // only works with custom-defined type
    public boolean isUnique() {
        return (flag & 0b0001) != 0;
    }
}
