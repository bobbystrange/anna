package org.dreamcat.anna.relaxed.controller.table;

import lombok.Data;
import org.dreamcat.anna.relaxed.entity.ColumnDefEntity;
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
public class CreateOrAlterTableQuery {
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
        private Boolean primary;
        private Boolean required;
        private Boolean unique;

        public int computeFlag() {
            return ColumnDefEntity.computeFlag(primary, required, unique);
        }
    }

    public int countColumnSize() {
        return columns.stream()
                .map(CreateOrAlterTableQuery.ColumnParam::getName)
                .collect(Collectors.toSet())
                .size();
    }
}