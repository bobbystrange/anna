package org.dreamcat.anna.relaxed.controller.view.query;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

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
    @Size(min = 1)
    private List<String> conditionArgs;
}
