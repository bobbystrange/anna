package org.dreamcat.anna.relaxed.controller.row.query;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Create by tuke on 2020/9/27
 */
@Data
public class DeleteFromQuery {

    @NotEmpty
    private String table;
    @NotEmpty
    private String value;
}
