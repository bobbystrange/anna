package org.dreamcat.anna.relaxed.controller.view.result;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.NameValuePair;

import java.util.List;

/**
 * Create by tuke on 2020/10/15
 */
@Data
public class QueryViewResult {
    private List<NameValuePair> fields;
}
