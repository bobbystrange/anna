package org.dreamcat.anna.relaxed.controller.row.result;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.core.NameValuePair;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class SelectFromResult {

    private List<NameValuePair> columns;
}
