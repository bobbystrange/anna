package org.dreamcat.anna.relaxed.component;

import java.util.Collection;

/**
 * Create by tuke on 2020/9/14
 */
public interface ReachabilityComponent {

    /**
     * test the validity of the expression
     *
     * @param expressions DSL expressions with related objects
     * @return the validity of the expression
     */
    boolean test(Collection<String> expressions, String tableName, String columnName, String condition);
}
