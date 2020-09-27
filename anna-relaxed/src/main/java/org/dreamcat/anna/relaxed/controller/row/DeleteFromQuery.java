package org.dreamcat.anna.relaxed.controller.row;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

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
