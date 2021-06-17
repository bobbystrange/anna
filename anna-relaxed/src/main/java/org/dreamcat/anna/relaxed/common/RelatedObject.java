package org.dreamcat.anna.relaxed.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumnField;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2020/9/16
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelatedObject {

    /**
     * real db column name
     */
    private String columnName;
    /**
     * real db table name
     */
    private String entityName;
    /**
     * related db column name
     */
    private String relatedColumnName;
    /**
     * nesting fields/objects
     */
    private Map<String, RelatedObject> children;
    /**
     * indexed db column
     */
    private Set<String> indexes;
    /**
     * include columns which will be selected
     */
    private Set<String> columnNames;

    public RelatedObject(String columnName) {
        this.columnName = columnName;
    }

    public static RelatedObject parse(Class<?> entityClass) {
        var node = new Node();
        parseNode(node, entityClass);
        return node.toRelatedObject();
    }

    private static void parseNode(Node node, Class<?> entityClass) {
        var relatedTable = ReflectUtil.retrieveAnnotation(entityClass, RelatedTable.class);
        if (relatedTable == null) {
            return;
        }

        // type
        var tableName = relatedTable.tableName();
        if (ObjectUtil.isBlank(tableName)) {
            return;
        }
        var nameColumnStrategy = relatedTable.nameColumnStrategy();
        var nameStyle = relatedTable.nameStyle();
        var columnStyle = relatedTable.columnStyle();
        var indexArray = relatedTable.index();

        RelatedObject relatedObject = new RelatedObject();
        node.relatedObject = relatedObject;
        node.entityClass = entityClass;

        relatedObject.setEntityName(tableName);
        var indexes = new HashSet<>(Arrays.asList(indexArray));
        relatedObject.setIndexes(indexes);

        var fields = ReflectUtil.retrieveNoStaticFields(entityClass);
        var columnNames = new HashSet<String>(fields.size() + indexes.size());
        columnNames.addAll(indexes);
        relatedObject.setColumnNames(columnNames);
        for (var field : fields) {
            var relatedColumn = field.getDeclaredAnnotation(RelatedColumn.class);
            if (relatedColumn != null) {
                var name = relatedColumn.name();
                var columnName = relatedColumn.column();
                var relatedColumnName = relatedColumn.relatedColumn();

                if (ObjectUtil.isBlank(name)) {
                    name = field.getName();
                }
                if (ObjectUtil.isBlank(columnName) || ObjectUtil.isBlank(relatedColumnName)) {
                    continue;
                }
                columnNames.add(columnName);

                var fieldType = field.getType();
                if (fieldType.equals(List.class) || fieldType.equals(Set.class)) {
                    fieldType = ReflectUtil.getTypeArgument(field);
                }
                if (node.hasLoop(fieldType)) {
                    continue;
                }

                var childNode = new Node(node, name);
                node.getChildNodes().add(childNode);
                parseNode(childNode, fieldType);
                RelatedObject child = childNode.relatedObject;
                if (child == null) {
                    continue;
                }

                child.setRelatedColumnName(columnName);
                child.setColumnName(relatedColumnName);
                continue;
            }

            String name = null, columnName = null;
            var relatedColumnField = field.getDeclaredAnnotation(RelatedColumnField.class);
            if (relatedColumnField != null) {
                name = relatedColumnField.name();
                columnName = relatedColumnField.column();
            }
            if (ObjectUtil.isBlank(name) && ObjectUtil.isBlank(columnName)) {
                columnName = name = field.getName();
                name = nameStyle.format(name);
                columnName = columnStyle.format(columnName);
            } else if (ObjectUtil.isBlank(name)) {
                name = switch (nameColumnStrategy) {
                    case FIELD_NAME -> field.getName();
                    case SIBLING_NAME -> columnName;
                };
                name = nameStyle.format(name);
            } else if (ObjectUtil.isBlank(columnName)) {
                columnName = switch (nameColumnStrategy) {
                    case FIELD_NAME -> field.getName();
                    case SIBLING_NAME -> name;
                };
                columnName = columnStyle.format(columnName);
            }
            columnNames.add(columnName);

            var childNode = new Node(node, name);
            node.getChildNodes().add(childNode);
            childNode.name = name;
            childNode.relatedObject = new RelatedObject(columnName);
        }
    }

    @NoArgsConstructor
    private static class Node {

        Node parent;
        Set<Node> childNodes;
        Class<?> entityClass;
        String name;
        RelatedObject relatedObject;

        Node(Node parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        boolean hasLoop(Class<?> clazz) {
            for (Node n = parent; n != null; n = n.parent) {
                if (n.entityClass == clazz) {
                    return true;
                }
            }
            return false;
        }

        Set<Node> getChildNodes() {
            if (childNodes == null) {
                childNodes = new HashSet<>();
            }
            return childNodes;
        }

        public RelatedObject toRelatedObject() {
            if (ObjectUtil.isNotEmpty(childNodes)) {
                var children = new HashMap<String, RelatedObject>();
                for (var child : childNodes) {
                    children.put(child.name, child.toRelatedObject());
                }
                relatedObject.children = children;
            }
            return relatedObject;
        }
    }
}
