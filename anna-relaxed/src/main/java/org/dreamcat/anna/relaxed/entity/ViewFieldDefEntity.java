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
@Entity(name = "view_field_def")
public class ViewFieldDefEntity extends BaseEntity {

    @Column(name = "view_id")
    private Long viewId;
    private String name;
    @Column(name = "display_name")
    private String displayName;
    private String expression;
}
