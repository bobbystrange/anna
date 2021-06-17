package org.dreamcat.anna.core.controller.table.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dreamcat.anna.core.common.query.ColumnIdQuery;

/**
 * Create by tuke on 2020/11/3
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreateIndexQuery extends ColumnIdQuery {
    private boolean unique;
    private List<String> names;
}
