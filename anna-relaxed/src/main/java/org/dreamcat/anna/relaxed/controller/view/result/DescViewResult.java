package org.dreamcat.anna.relaxed.controller.view.result;

import java.util.List;
import lombok.Data;

/**
 * Create by tuke on 2020/10/15
 */
@Data
public class DescViewResult {

    private String name;
    private String displayName;
    private List<FieldResult> fields;

    @Data
    public static class FieldResult {

        private String name;
        private String displayName;
        private String source;
        private Boolean primary;
        private Boolean required;
    }
}
