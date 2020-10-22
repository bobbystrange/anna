package org.dreamcat.anna.relaxed.component.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.anna.relaxed.core.RelatedObject;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/17
 * <p>
 * it will not be initialized until referenced by another bean or explicitly retrieved
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RelatedEntityComponentImpl implements RelatedEntityComponent {
    /**
     * (tableName, entityClass)
     */
    private Map<String, Class<?>> relatedObjectClasses;
    /**
     * (entityClass, relatedObject)
     */
    private Map<Class<?>, RelatedObject> relatedObjects;
    /**
     * (tableName, columnName, Repository)
     */
    private Map<String, Map<String, Function<Collection<Object>, List<Map<String, Object>>>>> entityRepositories;

    /// Dao
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> fetchEntities(String tableName, String columnName, Collection<Object> columnValues) {
        var repositories = entityRepositories.get(tableName);
        if (repositories == null) return Collections.emptyList();
        var repository = repositories.get(columnName);
        if (repository == null) return Collections.emptyList();
        return repository.apply(columnValues);
    }

    @Override
    public RelatedObject findByTableName(String tableName) {
        var entityClass = relatedObjectClasses.get(tableName);
        if (entityClass == null) return null;
        return relatedObjects.get(entityClass);
    }

    @PostConstruct
    public void init() {
        initRelatedObjects("");
        initEntityRepositories();
    }

    public void initRelatedObjects(Class<?> baseClass) {
        if (baseClass == null) initRelatedObjects("");
        else initRelatedObjects(baseClass.getPackageName());
    }

    public void initRelatedObjects(String packageName) {
        String name = packageName.replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException(
                    "the resource under the path " + packageName + " is not found");
        }
        File classFile = new File(url.getFile());
        String prefix = classFile.getPath() + "/";
        relatedObjects = new HashMap<>();
        findAndFillRelatedObjects(classFile, prefix, relatedObjects);
        relatedObjectClasses = relatedObjects.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getValue().getEntityName(),
                        Map.Entry::getKey));
    }

    public void initEntityRepositories() {
        entityRepositories = new HashMap<>();
        var entries = relatedObjectClasses.entrySet();
        for (var entry : entries) {
            var tableName = entry.getKey();
            var entityClass = entry.getValue();
            var relatedObject = relatedObjects.get(entityClass);
            if (relatedObject == null) continue;

            var columnNames = relatedObject.getColumnNames();
            if (ObjectUtil.isEmpty(columnNames)) continue;

            var indexes = relatedObject.getIndexes();
            var repositories = entityRepositories.computeIfAbsent(tableName, it -> new HashMap<>());
            var columnNameSql = String.join("`, `", columnNames);
            for (var index : indexes) {
                repositories.putIfAbsent(index, columnValues -> {
                    var sql = String.format("select `%s` from `%s` where `%s` in %s",
                            columnNameSql, tableName, index, concatInSql(columnValues));
                    return jdbcTemplate.query(
                            sql, (rs, rowNum) -> {
                                var map = new HashMap<String, Object>();
                                int i = 0;
                                for (var column : columnNames) {
                                    map.put(column, rs.getObject(++i));
                                }
                                return map;
                            });
                });
            }
        }
    }

    private static void findAndFillRelatedObjects(File classFile, String prefix, Map<Class<?>, RelatedObject> relatedObjectCache) {
        if (classFile.isDirectory()) {
            var files = classFile.listFiles();
            if (ObjectUtil.isEmpty(files)) return;
            for (var file : files) {
                findAndFillRelatedObjects(file, prefix, relatedObjectCache);
            }
            return;
        }
        if (!classFile.getPath().contains(".class")) return;

        var path = classFile.getPath();
        // 6 for .class
        var className = path.substring(prefix.length(), path.length() - 6)
                .replace("/", ".");
        try {
            var classObject = Class.forName(className);
            relatedObjectCache.computeIfAbsent(classObject, RelatedObject::parse);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private static String concatInSql(Collection<Object> args) {
        return "(" + args.stream().map(it -> {
            if (it instanceof Number) {
                return it.toString();
            } else if (it != null) {
                return String.format("\"%s\"", it);
            } else return String.valueOf((Object) null);
        }).collect(Collectors.joining(",")) + ")";
    }
}
