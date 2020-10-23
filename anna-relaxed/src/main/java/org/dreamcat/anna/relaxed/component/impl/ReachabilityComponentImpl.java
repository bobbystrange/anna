package org.dreamcat.anna.relaxed.component.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.component.ReachabilityComponent;
import org.dreamcat.anna.relaxed.component.RelatedEntityComponent;
import org.dreamcat.common.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class ReachabilityComponentImpl implements ReachabilityComponent {
    // Service
    private final RelatedEntityComponent relatedEntityService;

    @Override
    public boolean test(Collection<String> expressions, String entityName, String columnName, String condition) {
        var relatedObject = relatedEntityService.findByEntityName(entityName);
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
}
