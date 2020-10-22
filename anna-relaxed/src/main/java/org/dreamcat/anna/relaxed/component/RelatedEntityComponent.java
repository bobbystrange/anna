package org.dreamcat.anna.relaxed.component;


import org.dreamcat.anna.relaxed.core.RelatedObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Create by tuke on 2020/9/15
 */
public interface RelatedEntityComponent {

    RelatedObject findByTableName(String tableName);

    List<Map<String, Object>> fetchEntities(String tableName, String columnName, Collection<Object> columnValues);
}
