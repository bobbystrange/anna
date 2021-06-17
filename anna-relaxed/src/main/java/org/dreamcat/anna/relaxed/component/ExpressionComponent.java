package org.dreamcat.anna.relaxed.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dreamcat.anna.relaxed.common.RelatedObject;
import org.dreamcat.anna.relaxed.common.condition.ConditionArgContext;

/**
 * Create by tuke on 2020/10/22
 */
public interface ExpressionComponent {

    default List<?> parse(
            List<String> expressions, String entityName, String columnName,
            String columnValue, ConditionArgContext context) {
        // (entityName, columnName, columnValues)
        var entityCache = new HashMap<String, Map<String, Map<Object, List<Map<String, Object>>>>>();
        return parse(expressions, entityName, columnName, columnValue, context, entityCache);
    }

    /**
     * evaluate expressions
     *
     * @param expressions list which contains the DSL expressions with related objects
     * @param entityName  the entity which will be selected
     * @param columnName  the name of the related column
     * @param columnValue the value of the related column
     * @param entityCache (entityName, columnName, columnValues), you can pass a empty mutable map
     * @return values which will be evaluated dynamically
     */
    List<?> parse(
            List<String> expressions, String entityName, String columnName,
            String columnValue, ConditionArgContext context,
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache);

    List<?> parse(
            List<String> expressions, RelatedObject relatedObject,
            String columnValue, ConditionArgContext context,
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache);

    Map<String, ?> parseAsMap(
            List<String> expressions, String entityName, String columnName,
            String columnValue, ConditionArgContext context);

    Map<String, ?> parseAsMap(
            List<String> expressions, RelatedObject relatedObject,
            String columnValue, ConditionArgContext context);

}
