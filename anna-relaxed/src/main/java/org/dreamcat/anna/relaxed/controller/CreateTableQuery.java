package org.dreamcat.anna.relaxed.controller;

import lombok.Data;
import org.dreamcat.anna.relaxed.entity.strategy.ColumnType;
import org.dreamcat.common.web.jackson.GenericDeserialize;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/12
 */
@Data
public class CreateTableQuery {
    @NotEmpty
    @Pattern(regexp = "[a-z][a-z_]+?")
    private String name;
    private String displayName;
    @NotEmpty
    private List<ColumnParam> columns;

    @Data
    public static class ColumnParam {
        @NotEmpty
        private String name;
        private String displayName;
        @NotEmpty
        @GenericDeserialize
        private ColumnType type;
        private Boolean required;
        private Boolean unique;
    }

    public int countColumnSize() {
        return columns.stream()
                .map(CreateTableQuery.ColumnParam::getName)
                .collect(Collectors.toSet())
                .size();
    }
}
