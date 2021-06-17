package org.dreamcat.anna.core.controller.table.query;

import static org.dreamcat.anna.core.config.ValidationConfig.TABLE_COLUMN_NAME_MESSAGE;
import static org.dreamcat.anna.core.config.ValidationConfig.TABLE_COLUMN_NAME_PATTERN;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dreamcat.anna.core.common.enums.ColumnType;
import org.dreamcat.anna.core.common.query.TableIdQuery;

/**
 * Create by tuke on 2020/11/2
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AddColumnQuery extends TableIdQuery {

    /**
     * name identifier, immutable and unique in single tenant scope
     */
    @NotEmpty(message = TABLE_COLUMN_NAME_MESSAGE)
    @Pattern(regexp = TABLE_COLUMN_NAME_PATTERN)
    private String name;
    private String alias;
    private ColumnType type;
    // like decimal(16,6), varchar(100)
    private List<Integer> typeValues;
    private boolean nullable;
    private String defaultValue;
    private String comment;
}
