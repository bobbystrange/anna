package org.dreamcat.anna.core.entity;

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
@Entity(name = "table_def")
public class TableDefEntity extends BaseEntity {

    private String name;
    private String alias;
    private String comment;
    private boolean fulfilled;
}
