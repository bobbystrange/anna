package org.dreamcat.anna.relaxed.core;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class ValueStrategy {
    private Long tenantId;
    // for cache purpose only, to simplify the parsing methods
    private transient Long columnId;
    private Map<Long, Set<String>> selectMap;

    public ValueStrategy atColumn(Long columnId) {
        this.columnId = columnId;
        return this;
    }
}
