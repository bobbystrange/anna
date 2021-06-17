package org.dreamcat.anna.core.controller.table.query;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dreamcat.anna.core.common.query.TableIdQuery;

/**
 * Create by tuke on 2021/1/30
 */
@Getter
@Setter
@ToString
public class AlterTableQuery extends TableIdQuery {

    /**
     * display name, default using <code>name</code> to display
     */
    @Size(max = 64)
    private String alias;
    /**
     * comment, just a note
     */
    @Size(max = 1000)
    private String comment;
}
