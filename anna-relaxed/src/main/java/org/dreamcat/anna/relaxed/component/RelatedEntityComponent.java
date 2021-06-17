package org.dreamcat.anna.relaxed.component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.dreamcat.anna.relaxed.common.RelatedObject;

/**
 * Create by tuke on 2020/9/15
 */
public interface RelatedEntityComponent {

    RelatedObject findByEntityName(String entityName);

    List<Map<String, Object>> fetchEntities(String entityName, String columnName,
            Collection<Object> columnValues);
}
