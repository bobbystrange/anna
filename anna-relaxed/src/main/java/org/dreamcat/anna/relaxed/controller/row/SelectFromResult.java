package org.dreamcat.anna.relaxed.controller.row;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.NameValuePair;

import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class SelectFromResult {
    private List<NameValuePair> columns;
}