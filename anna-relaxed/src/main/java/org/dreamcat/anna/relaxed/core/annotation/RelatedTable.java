package org.dreamcat.anna.relaxed.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/9/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelatedTable {

    String tableName();

    String[] index() default {"id"};

    Strategy nameColumnStrategy() default Strategy.FIELD_NAME;

    Style nameStyle() default Style.NONE;

    Style columnStyle() default Style.NONE;

    enum Strategy {
        FIELD_NAME,
        SIBLING_NAME;
    }

    enum Style {
        NONE,
        CAMEL,
        CAMEL_CASE_CAPITALIZED,
        SNAKE;

        public String format(String s) {
            return switch (this) {
                case SNAKE -> StringUtil.toSnakeCase(s);
                case CAMEL -> StringUtil.toCamelCase(s);
                case CAMEL_CASE_CAPITALIZED -> StringUtil.toCapitalCamelCase(s);
                default -> s;
            };
        }
    }
}
