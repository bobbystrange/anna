package org.dreamcat.anna.relaxed.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.web.data.IntEnumAttribute;
import org.dreamcat.common.web.data.IntEnumAttributeConverter;

/**
 * Create by tuke on 2020/9/12
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ColumnType implements IntEnumAttribute {
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

    @javax.persistence.Converter(autoApply = true)
    public static class Converter extends IntEnumAttributeConverter<ColumnType> {
        public Converter() {
            super(ColumnType.class);
        }
    }
}
