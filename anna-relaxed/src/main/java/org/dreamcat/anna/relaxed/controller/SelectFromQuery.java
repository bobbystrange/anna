package org.dreamcat.anna.relaxed.controller;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class SelectFromQuery {
    @NotEmpty
    private String table;
    @NotEmpty
    private String value;
    @Size(min = 1)
    private List<String> columns;
}
