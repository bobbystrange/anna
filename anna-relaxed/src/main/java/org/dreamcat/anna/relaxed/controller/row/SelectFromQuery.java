package org.dreamcat.anna.relaxed.controller.row;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class SelectFromQuery extends DeleteFromQuery {
    @Size(min = 1)
    private List<String> columns;
}
