package org.dreamcat.anna.relaxed.component.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.anna.relaxed.component.ExpressionComponent;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.anna.relaxed.core.RelatedObject;
import org.dreamcat.anna.relaxed.core.condition.ConditionArgContext;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.stereotype.Component;

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
 * Create by tuke on 2020/10/22
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ExpressionComponentImpl implements ExpressionComponent {
    private final RelatedEntityComponent relatedEntityComponent;

    @Override
    public List<?> parse(
            List<String> expressions, String tableName, String columnName,
            String columnValue, ConditionArgContext context,
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache) {
        var relatedObject = relatedEntityComponent.findByTableName(tableName);
        if (relatedObject == null || ObjectUtil.isEmpty(relatedObject.getChildren())) {
            // a all-null-element list has same size with expressions
            return Arrays.asList(new Void[expressions.size()]);
        }
        relatedObject.setColumnName(columnName);
        return parse(expressions, relatedObject, columnValue, context, entityCache);
    }

    @Override
    public List<?> parse(
            List<String> expressions, RelatedObject relatedObject,
            String columnValue, ConditionArgContext context,
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache) {
        var list = new ArrayList<>();
        outer:
        for (var expression : expressions) {
            var fields = expression.split("\\.");
            var entityName = relatedObject.getEntityName();
            var entityColumnName = relatedObject.getColumnName();
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
    public Map<String, ?> parseAsMap(
            List<String> expressions, String tableName, String columnName,
            String columnValue, ConditionArgContext context) {
        var relatedObject = relatedEntityComponent.findByTableName(tableName);
        if (relatedObject == null || ObjectUtil.isEmpty(relatedObject.getChildren())) {
            // a all-null-element list has same size with expressions
            return new HashMap<>();
        }
        relatedObject.setColumnName(columnName);
        return parseAsMap(expressions, relatedObject, columnValue, context);
    }

    @Override
    public Map<String, ?> parseAsMap(
            List<String> expressions, RelatedObject relatedObject,
            String columnValue, ConditionArgContext context) {
        // (entityName, columnName, columnValues)
        var entityCache = new HashMap<String, Map<String, Map<Object, List<Map<String, Object>>>>>();
        // fetch from db
        parse(expressions, relatedObject, columnValue, context, entityCache);
        // build from entityCache
        var entityName = relatedObject.getEntityName();
        var entityColumnName = relatedObject.getColumnName();
        Set<Object> columnValues = Collections.singleton(columnValue);
        var entities = fetchEntities(entityCache, entityName, entityColumnName, columnValues);
        Map<String, Object> map = new HashMap<>();
        if (entities.size() != 1) {
            return map;
        }
        var entity = entities.get(0);
        for (var expression : expressions) {
            var fields = expression.split("\\.");
            var size = fields.length;
            recurseParseAsMap(map, fields, 0, size,
                    relatedObject, entity, entityCache);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void recurseParseAsMap(
            Map<String, Object> map,
            String[] fields, int offset, int size,
            // String entityName, String entityColumnName, Set<Object> columnValues,
            RelatedObject relatedObject, Map<String, Object> entity,
            HashMap<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache) {
        var field = fields[offset];
        var child = relatedObject.getChildren().get(field);
        // no relationship
        if (child == null) return;

        var children = child.getChildren();
        if (ObjectUtil.isEmpty(children)) {
            if (offset < size - 1) return;

            var relatedFieldName = child.getColumnName();
            var value = entity.get(relatedFieldName);
            map.put(field, value);
        } else {
            if (offset == size - 1) return;

            var relatedColumn = child.getRelatedColumnName();
            var value = entity.get(relatedColumn);

            // for next loop
            offset++;
            var entityName = child.getEntityName();
            var entityColumnName = child.getColumnName();

            var entities = fetchEntities(entityCache, entityName, entityColumnName, Collections.singleton(value));
            if (ObjectUtil.isEmpty(entities)) return;

            if (entities.size() == 1) {
                var nextMap = (Map<String, Object>) map.computeIfAbsent(field, it -> new HashMap<String, Object>());
                var e = entities.get(0);
                recurseParseAsMap(nextMap, fields, offset, size,
                        child, e, entityCache);
            } else {
                var list = (List<Map<String, Object>>) map.computeIfAbsent(field, it -> new ArrayList<Map<String, Object>>());
                var listSize = list.size();
                for (int i = 0, len = entities.size(); i < len; i++) {
                    var e = entities.get(i);
                    Map<String, Object> nextMap;
                    if (listSize == 0) {
                        nextMap = new HashMap<String, Object>();
                        list.add(nextMap);
                    } else if (listSize > i) {
                        nextMap = list.get(i);
                    } else {
                        continue;
                    }
                    recurseParseAsMap(nextMap, fields, offset, size,
                            child, e, entityCache);
                }
            }
        }
    }

    private List<Map<String, Object>> fetchEntities(
            Map<String, Map<String, Map<Object, List<Map<String, Object>>>>> entityCache, String entityName,
            String columnName, Set<Object> columnValues) {
        var cache = entityCache.computeIfAbsent(entityName, entityNameKey -> new HashMap<>())
                .computeIfAbsent(columnName, columnNameKey -> new HashMap<>());

        var differentSet = new HashSet<>(columnValues);
        differentSet.removeAll(cache.keySet());
        if (!differentSet.isEmpty()) {
            log.info("fetch entities: entityName={}, columnName={}, columnValues={}", entityName, columnName, columnValues);
            var entities = relatedEntityComponent.fetchEntities(entityName, columnName, differentSet);
            var entityMap = entities.stream()
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
