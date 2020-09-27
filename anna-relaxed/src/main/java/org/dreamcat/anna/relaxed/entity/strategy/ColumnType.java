package org.dreamcat.anna.relaxed.entity.strategy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/9/12
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ColumnType {
    SOURCE(0),
    TEXT(1),
    NUMERIC(2),
    DATE(3),
    TIME(4),
    DATETIME(5),
    ATTACHMENT(6),
    SELECT_ONE(7),
    SELECT_MANY(8),
    SELECT_ONE_NUMERIC(9),
    SELECT_MANY_NUMERIC(10);

    private final int value;
}
