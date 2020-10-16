package org.dreamcat.anna.relaxed.controller.view.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Create by tuke on 2020/10/15
 */
@Data
public class QueryViewQuery {
    private String view;
    @NotEmpty
    private String value;
    @Size(min = 1)
    private List<String> fields;
}
