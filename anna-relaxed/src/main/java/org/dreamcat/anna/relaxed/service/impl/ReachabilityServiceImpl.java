package org.dreamcat.anna.relaxed.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.service.ReachabilityService;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class ReachabilityServiceImpl implements ReachabilityService {
    // Service
    private final RelatedEntityComponent relatedEntityService;

    @Override
    public boolean test(String expression, String tableName) {
        var relatedObject = relatedEntityService.findByTableName(tableName);
        if (relatedObject == null) {
            return false;
        }

        var relatedObjects = relatedObject.getChildren();
        if (ObjectUtil.isEmpty(relatedObjects)) {
            return false;
        }

        var fields = expression.split("\\.");
        for (int i = 0, size = fields.length; i<size; i++) {
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

        return true;
    }

    @Override
    public List<?> parse(List<String> expressions, String tableName, String columnName, String columnValue) {
        var relatedObject = relatedEntityService.findByTableName(tableName);
        if (relatedObject == null || ObjectUtil.isEmpty(relatedObject.getChildren())) {
            // a all-null-element list has same size with expressions
            return Arrays.asList(new Void[expressions.size()]);
        }

        // (entityName, columnName, columnValues)
        var entityCache = new HashMap<String, List<Map<String, Object>>>();
        var list = new ArrayList<>();
        outer: for (var expression: expressions) {
            var fields = expression.split("\\.");
            var entityName = relatedObject.getEntityName();
            Collection<Object> columnValues = Collections.singleton(columnValue);
            var entityRelatedObject = relatedObject;
            for (int i = 0, size = fields.length; i<size; i++) {
                var field = fields[i];
                var child = entityRelatedObject.getChildren().get(field);
                if (child == null) {
                    list.add(null);
                    continue outer;
                }

                var children = child.getChildren();
                if (ObjectUtil.isEmpty(children)) {
                    if (i < size - 1) {
                        list.add(null);
                        continue outer;
                    }

                    var relatedFieldName = child.getColumnName();
                    var entities = fetchEntities(entityCache, entityName, columnName, columnValues);
                    if (ObjectUtil.isEmpty(entities)) {
                        list.add(null);
                        continue outer;
                    }
                    var values = entities.stream()
                            .map(it -> it.get(relatedFieldName))
                            .collect(Collectors.toList());
                    if (values.size() == 1) {
                        list.add(values.get(0));
                    } else {
                        list.add(values);
                    }
                } else {
                    if (i == size - 1) {
                        list.add(null);
                        continue outer;
                    }
                    var entities = fetchEntities(entityCache, entityName, columnName, columnValues);
                    if (ObjectUtil.isEmpty(entities)) {
                        list.add(null);
                        continue outer;
                    }

                    entityRelatedObject = child;
                    var relatedColumn = entityRelatedObject.getRelatedColumnName();
                    var values = entities.stream()
                            .map(it -> it.get(relatedColumn))
                            .collect(Collectors.toSet());
                    // for next loop
                    entityName = entityRelatedObject.getEntityName();
                    columnName = entityRelatedObject.getColumnName();
                    columnValues = values;
                }
            }
        }
        return list;
    }

    private List<Map<String, Object>> fetchEntities(
            Map<String, List<Map<String, Object>>> entityCache, String entityName,
            String columnName, Collection<Object> columnValues) {
        return entityCache.computeIfAbsent(entityName, it ->
                relatedEntityService.fetchEntities(it, columnName, columnValues));
    }

}
