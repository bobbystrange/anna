package org.dreamcat.anna.core.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/12/14
 */
public final class ParameterValidityUtil {

    public static <T> boolean hasDuplicateValues(
            Collection<T> collection, Function<T, String> getter) {
        return collection.size() != collection.stream()
                .map(getter)
                .distinct()
                .count();
    }

}
