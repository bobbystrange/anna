package org.dreamcat.anna.relaxed.entity.strategy;

import lombok.Getter;
import lombok.Setter;
import org.dreamcat.anna.relaxed.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Create by tuke on 2020/9/12
 */
@Getter
@Setter
@Table
@Entity(name = "select_value_strategy")
public class SelectValueStrategy extends BaseEntity {
    @Column(name = "table_id")
    private Long tableId;
    @Column(name = "column_id")
    private Long columnId;
    private String value;
}
