package org.dreamcat.anna.relaxed.controller;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class InsertIntoQuery {
    private String table;

    @NotEmpty
    private List<ColumnParam> columns;

    @Data
    public static class ColumnParam {
        @NotEmpty
        private String name;
        // Number, String, List<Number> or List<String>
        @NotNull
        private Object value;
    }

    public int countColumnSize() {
        return columns.stream()
                .map(InsertIntoQuery.ColumnParam::getName)
                .collect(Collectors.toSet())
                .size();
    }
}
