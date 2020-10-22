package org.dreamcat.anna.relaxed.component.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.component.ReachabilityComponent;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class ReachabilityComponentImpl implements ReachabilityComponent {
    // Service
    private final RelatedEntityComponent relatedEntityService;

    @Override
    public boolean test(Collection<String> expressions, String tableName, String columnName, String condition) {
        var relatedObject = relatedEntityService.findByTableName(tableName);
        if (relatedObject == null) {
            return false;
        }

        var relatedObjects = relatedObject.getChildren();
        if (ObjectUtil.isEmpty(relatedObjects)) {
            return false;
        }
        if (!relatedObjects.containsKey(columnName)) {
            return false;
        }

        for (var expression : expressions) {
            var fields = expression.split("\\.");
            for (int i = 0, size = fields.length; i < size; i++) {
                var field = fields[i];
                var object = relatedObjects.get(field);
                if (object == null) return false;

                var children = object.getChildren();
                if (ObjectUtil.isEmpty(children)) {
                    if (i < size - 1) return false;
                } else {
                    if (i == size - 1) return false;
                    relatedObjects = children;
                }
            }
        }
        return true;
    }

    private List<Map<String, Object>> fetchEntities(
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache, String entityName,
            String columnName, Set<Object> columnValues) {
        var cache = entityCache.computeIfAbsent(entityName, entityNameKey -> new HashMap<>())
                .computeIfAbsent(columnName, columnNameKey -> new HashMap<>());

        var differentSet = new HashSet<>(columnValues);
        differentSet.removeAll(cache.keySet());
        if (!differentSet.isEmpty()) {
            List<Map<String, Object>> entities = relatedEntityService.fetchEntities(entityName, columnName, differentSet);
            Map<Object, List<Map<String, Object>>> entityMap = entities.stream()
                    .collect(Collectors.groupingBy(entity -> entity.get(columnName)));
            cache.putAll(entityMap);
            // no filtering
            return entityMap.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        return cache.values().stream()
                .flatMap(Collection::stream)
                .filter(it -> columnValues.contains(it.get(columnName)))
                .collect(Collectors.toList());
    }

}
