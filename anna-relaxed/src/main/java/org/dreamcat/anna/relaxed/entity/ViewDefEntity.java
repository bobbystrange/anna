package org.dreamcat.anna.relaxed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by tuke on 2020/10/15
 */
@Getter
@Setter
@Table
@Entity(name = "view_def")
public class ViewDefEntity extends BaseEntity {

    private String name;
    @Column(name = "display_name")
    private String displayName;
    // a physical database table
    @Column(name = "source_table")
    private String sourceTable;
    @Column(name = "source_column")
    private String sourceColumn;
    // such as  A.type = ? and length(A.B.name) > 0
    @Column(name = "source_condition")
    private String sourceCondition;
}
