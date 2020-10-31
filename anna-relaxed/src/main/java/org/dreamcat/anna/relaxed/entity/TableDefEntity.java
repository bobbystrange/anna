package org.dreamcat.anna.relaxed.entity;

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
@Entity(name = "table_def")
public class TableDefEntity extends BaseEntity {

    private String name;
    @Column(name = "display_name")
    private String displayName;
}
