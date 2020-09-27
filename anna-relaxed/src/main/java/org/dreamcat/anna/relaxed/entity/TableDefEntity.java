package org.dreamcat.anna.relaxed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Create by tuke on 2020/9/11
 */
@Getter
@Setter
@Table
@Entity(name = "table_def")
public class TableDefEntity extends BaseEntity {
    private String name;
    @Column(name = "display_name")
    private String displayName;
    // a physical database table
    @Column(name = "source_table")
    private String sourceTable;
    @Column(name = "source_column")
    private String sourceColumn;
}
