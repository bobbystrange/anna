package org.dreamcat.anna.core.controller.table.query;

import static org.dreamcat.anna.core.config.ValidationConfig.TABLE_NAME_MESSAGE;
import static org.dreamcat.anna.core.config.ValidationConfig.TABLE_NAME_PATTERN;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Create by tuke on 2020/10/31
 */
@Getter
@Setter
@ToString
public class CreateTableQuery {

    /**
     * name identifier, immutable and unique in single tenant scope
     * preferred MySQL, since include/mysql_com.h: define NAME_CHAR_LEN 64
     * @link https://dev.mysql.com/doc/refman/8.0/en/identifier-length.html
     */
    @NotEmpty(message = TABLE_NAME_MESSAGE)
    @Pattern(regexp = TABLE_NAME_PATTERN)
    private String name;
    /**
     * display name, default using {@link #name}} to display
     */
    @Size(max = 64)
    private String alias;
    /**
     * comment, just a note
     */
    @Size(max = 1000)
    private String comment;
}
