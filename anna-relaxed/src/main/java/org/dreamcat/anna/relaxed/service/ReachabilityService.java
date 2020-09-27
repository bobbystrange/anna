package org.dreamcat.anna.relaxed.service;

import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
public interface ReachabilityService {

    /**
     * test the validity of the expression
     *
     * @param expression a DSL expression with related objects
     * @return the validity of the expression
     */
    boolean test(String expression, String tableName);

    /**
     * evaluate expressions
     *
     * @param expressions list which contains the DSL expressions with related objects
     * @param tableName   the table which will be selected
     * @param columnName  the name of the related column
     * @param columnValue the value of the related column
     * @return values which will be evaluated dynamically
     */
    List<?> parse(List<String> expressions, String tableName, String columnName, String columnValue);
}
