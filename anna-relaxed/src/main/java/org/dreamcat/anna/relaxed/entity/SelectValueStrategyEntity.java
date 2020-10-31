package org.dreamcat.anna.relaxed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by tuke on 2020/9/12
 */
@Getter
@Setter
@Table
@Entity(name = "select_value_strategy")
public class SelectValueStrategyEntity extends BaseEntity {

    @Column(name = "table_id")
    private Long tableId;
    @Column(name = "column_id")
    private Long columnId;
    private String value;
}
