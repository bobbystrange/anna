package org.dreamcat.anna.relaxed.controller.table;

import lombok.Data;
import org.dreamcat.anna.relaxed.entity.strategy.ColumnType;

import java.util.List;

/**
 * Create by tuke on 2020/9/27
 */
@Data
public class DescTableResult {
    private String name;
    private String displayName;
    private List<ColumnResult> columns;

    @Data
    public static class ColumnResult {
        private String name;
        private String displayName;
        private ColumnType type;
        private String source;
        private Boolean primary;
        private Boolean required;
    }
}
