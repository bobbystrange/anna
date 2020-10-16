package org.dreamcat.anna.relaxed.controller.row.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/14
 */
@Data
public class InsertIntoOrUpdateSetQuery {
    private String table;
    @NotEmpty
    private List<Column> columns;

    @Data
    public static class Column {
        @NotEmpty
        private String name;
        /**
         * one of Number, String, List<Number> or List<String>
         */
        @NotNull
        private Object value;
    }

    public Map<String, Column> getColumnMap() {
        return columns.stream().collect(Collectors.toMap(Column::getName, Function.identity()));
    }

    public int countColumnSize() {
        return (int) columns.stream().map(Column::getName).distinct().count();
    }
}
