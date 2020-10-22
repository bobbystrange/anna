package org.dreamcat.anna.relaxed.controller.view.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Create by tuke on 2020/10/15
 */
@Data
public class CreateOrAlterViewQuery {
    @NotEmpty
    @Pattern(regexp = "[A-Za-z][A-Za-z_]+?")
    private String name;
    @NotEmpty
    private String displayName;
    @NotEmpty
    @Pattern(regexp = "[A-Za-z][A-Za-z_]+?")
    private String sourceTable;
    @NotEmpty
    @Pattern(regexp = "[A-Za-z][A-Za-z_]+?")
    private String sourceColumn;
    private String sourceCondition;
    @NotEmpty
    private List<FieldParam> fields;

    @Data
    public static class FieldParam {
        @NotEmpty
        private String name;
        @NotEmpty
        private String displayName;
        @NotEmpty
        private String expression;
    }

    public int countFieldSize() {
        return (int) fields.stream()
                .map(FieldParam::getName)
                .distinct().count();
    }
}
