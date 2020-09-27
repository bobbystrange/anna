package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;
import org.dreamcat.anna.relaxed.entity.strategy.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

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
    // <unique> <required> <primary>
    private Integer flag;

    // the related column on the source table
    public boolean isPrimary() {
        return (flag & 1) != 0;
    }

    // only works with custom-defined type
    public boolean isRequired() {
        return (flag & 1 << 1) != 0;
    }

    // only works with custom-defined type
    public boolean isUnique() {
        return (flag & 1 << 2) != 0;
    }

    public static int computeFlag(Boolean primary, Boolean required, Boolean unique) {
        int flag = 0;
        if (Objects.equals(primary, true)) flag |= 1;
        if (Objects.equals(required, true)) flag |= 1 << 1;
        if (Objects.equals(unique, true)) flag |= 1 << 2;
        return flag;
    }
}
