package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.common.ColumnType;
import org.dreamcat.anna.relaxed.common.ValueStrategy;

/**
 * Create by tuke on 2020/9/14
 */
public interface RowValueService {

    Object parseLiteral(String value, ColumnType type);

    String formatToLiteral(Object value, ColumnType type, ValueStrategy valueStrategy);
}
