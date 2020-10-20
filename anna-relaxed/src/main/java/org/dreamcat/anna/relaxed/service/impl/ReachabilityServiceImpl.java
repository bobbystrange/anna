package org.dreamcat.anna.relaxed.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.anna.relaxed.core.condition.ConditionArgContext;
import org.dreamcat.anna.relaxed.service.ReachabilityService;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

        return true;
    }

    @Override
    public List<?> parse(List<String> expressions, String tableName, String columnName, String columnValue, ConditionArgContext context) {
        var relatedObject = relatedEntityService.findByTableName(tableName);
        if (relatedObject == null || ObjectUtil.isEmpty(relatedObject.getChildren())) {
            // a all-null-element list has same size with expressions
            return Arrays.asList(new Void[expressions.size()]);
        }

        // (entityName, columnName, columnValues)
        var entityCache = new HashMap<String, Map<String, Map<Object, List<Map<String, Object>>>>>();
        var list = new ArrayList<>();
        outer:
        for (var expression : expressions) {
            var fields = expression.split("\\.");
            var entityName = relatedObject.getEntityName();
            var entityColumnName = columnName;
            Set<Object> columnValues = Collections.singleton(columnValue);
            var entityRelatedObject = relatedObject;
            for (int i = 0, size = fields.length; i < size; i++) {
                var field = fields[i];
                var child = entityRelatedObject.getChildren().get(field);
                // no relationship
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
                    var entities = fetchEntities(entityCache, entityName, entityColumnName, columnValues);
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
                    var entities = fetchEntities(entityCache, entityName, entityColumnName, columnValues);
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
                    entityColumnName = entityRelatedObject.getColumnName();
                    columnValues = values;
                }
            }
        }
        return list;
    }

    @Override
    public Map<String, ?> parseAsMap(List<String> expressions, String tableName, String columnName, String columnValue, ConditionArgContext conditionArgs) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, ?> parseAsExampleMap(List<String> expressions, String tableName, String columnName, String columnValue, ConditionArgContext context) {
        var relatedObject = relatedEntityService.findByTableName(tableName);
        if (relatedObject == null || ObjectUtil.isEmpty(relatedObject.getChildren())) {
            return Collections.emptyMap();
        }

        // (entityName, columnName, columnValues)
        var entityCache = new HashMap<String, Map<String, Map<Object, List<Map<String, Object>>>>>();
        var map = new HashMap<String, Object>();
        outer:
        for (var expression : expressions) {
            var fields = expression.split("\\.");
            var entityName = relatedObject.getEntityName();
            String entityColumnName = columnName;
            Set<Object> columnValues = Collections.singleton(columnValue);
            var entityRelatedObject = relatedObject;
            Map<String, Object> currentMap = map;
            for (int i = 0, size = fields.length; i < size; i++) {
                var field = fields[i];
                var child = entityRelatedObject.getChildren().get(field);
                if (child == null) continue outer;

                var children = child.getChildren();
                if (ObjectUtil.isEmpty(children)) {
                    if (i < size - 1) continue outer;

                    var relatedFieldName = child.getColumnName();
                    var entities = fetchEntities(entityCache, entityName, entityColumnName, columnValues);
                    if (ObjectUtil.isEmpty(entities)) continue outer;

                    var values = entities.stream()
                            .map(it -> it.get(relatedFieldName))
                            .collect(Collectors.toList());
                    if (values.size() == 1) {
                        currentMap.put(field, values.get(0));
                    } else {
                        currentMap.put(field, values);
                    }
                } else {
                    if (i == size - 1) continue outer;
                    var entities = fetchEntities(entityCache, entityName, entityColumnName, columnValues);
                    if (ObjectUtil.isEmpty(entities)) continue outer;

                    entityRelatedObject = child;
                    var relatedColumn = entityRelatedObject.getRelatedColumnName();
                    var values = entities.stream()
                            .map(it -> it.get(relatedColumn))
                            .collect(Collectors.toSet());
                    // for next loop
                    entityName = entityRelatedObject.getEntityName();
                    entityColumnName = entityRelatedObject.getColumnName();
                    columnValues = values;

                    var previousMap = currentMap;
                    var mapOnField = previousMap.get(field);
                    if (mapOnField == null) {
                        currentMap = new HashMap<>();
                        previousMap.put(field, currentMap);
                    } else {
                        currentMap = (Map<String, Object>) mapOnField;
                    }
                }
            }
        }
        return map;
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
