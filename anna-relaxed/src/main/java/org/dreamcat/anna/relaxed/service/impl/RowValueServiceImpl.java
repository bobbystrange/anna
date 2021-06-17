package org.dreamcat.anna.relaxed.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.common.ColumnType;
import org.dreamcat.anna.relaxed.common.ValueStrategy;
import org.dreamcat.anna.relaxed.dao.SelectValueStrategyDao;
import org.dreamcat.anna.relaxed.entity.SelectValueStrategyEntity;
import org.dreamcat.anna.relaxed.service.RowValueService;
import org.dreamcat.common.text.NumericSearcher;
import org.dreamcat.common.util.TimeUtil;
import org.dreamcat.common.web.exception.BadRequestException;
import org.dreamcat.common.x.jackson.JacksonUtil;
import org.springframework.stereotype.Service;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class RowValueServiceImpl implements RowValueService {

    private final SelectValueStrategyDao selectValueStrategyDao;

    @Override
    public Object parseLiteral(String value, ColumnType type) {
        value = value.trim();
        switch (type) {
            case TEXT, SELECT_ONE, ATTACHMENT -> {
                return value;
            }
            case NUMERIC -> {
                var pair = NumericSearcher.extractNumber(value);
                // value is not a number
                if (pair.second() != value.length()) {
                    return null;
                }
                return pair.first();
            }
            case DATE -> {
                return LocalDate.parse(value).format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            case TIME -> {
                return LocalTime.parse(value).format(DateTimeFormatter.ISO_LOCAL_TIME);
            }
            case DATETIME, SELECT_ONE_NUMERIC -> {
                return Long.valueOf(value);
            }
            case SELECT_MANY -> {
                return JacksonUtil.fromJsonArray(value, String.class);
            }
            case SELECT_MANY_NUMERIC -> {
                return JacksonUtil.fromJsonArray(value, Long.class);
            }
        }
        // never reach here
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String formatToLiteral(Object value, ColumnType type, ValueStrategy valueStrategy) {
        var tenantId = valueStrategy.getTenantId();
        var columnId = valueStrategy.getColumnId();
        switch (type) {
            case TEXT, NUMERIC, ATTACHMENT -> {
                return value.toString();
            }
            case DATE -> {
                if (value instanceof String) {
                    try {
                        return LocalDate.parse((String) value).format(
                                DateTimeFormatter.ISO_LOCAL_DATE);
                    } catch (Exception ignore) {
                    }
                }
                throw new BadRequestException("require a date string");
            }
            case TIME -> {
                if (value instanceof String) {
                    return LocalTime.parse((String) value).format(DateTimeFormatter.ISO_LOCAL_TIME);
                }
                throw new BadRequestException("require a time string");
            }
            case DATETIME -> {
                if (value instanceof String) {
                    try {
                        return String.valueOf(TimeUtil.asEpochMilli(
                                LocalDateTime.parse((String) value)));
                    } catch (Exception ignore) {
                    }
                } else if (value instanceof Number) {
                    return String.valueOf(((Number) value).longValue());
                }
                throw new BadRequestException("require a datetime string or a epoch number");
            }
            case SELECT_ONE, SELECT_ONE_NUMERIC, SELECT_MANY, SELECT_MANY_NUMERIC -> {
                checkSelectType(value, type);

                var selectMap = valueStrategy.getSelectMap();
                if (selectMap == null) {
                    selectMap = new HashMap<>();
                    valueStrategy.setSelectMap(selectMap);
                }

                var selectValues = selectMap.get(columnId);
                if (selectValues == null) {
                    selectValues = selectValueStrategyDao
                            .findAllByTenantIdAndColumnId(tenantId, columnId).stream()
                            .map(SelectValueStrategyEntity::getValue).collect(Collectors.toSet());
                    selectMap.put(columnId, selectValues);
                }

                if (type.equals(ColumnType.SELECT_ONE) || type.equals(
                        ColumnType.SELECT_ONE_NUMERIC)) {
                    var s = value.toString();
                    if (!selectValues.contains(s)) {
                        throw new BadRequestException(String.format(
                                "require a specified value but got '%s'", s));
                    }
                    return s;
                } else if (type.equals(ColumnType.SELECT_MANY)) {
                    var list = (List<String>) value;
                    for (var s : list) {
                        if (!selectValues.contains(s)) {
                            throw new BadRequestException(String.format(
                                    "require a specified value in list but got '%s'", s));
                        }
                    }
                    return JacksonUtil.toJson(list);
                } else {
                    var list = (List<Number>) value;
                    for (var n : list) {
                        if (!selectValues.contains(n.toString())) {
                            throw new BadRequestException(String.format(
                                    "require a specified value in list but got '%s'", n));
                        }
                    }
                    return JacksonUtil.toJson(list);
                }
            }
        }
        // never reach here
        return null;
    }

    private void checkSelectType(Object value, ColumnType type) {
        if (type.equals(ColumnType.SELECT_ONE)) {
            if (!(value instanceof String)) {
                throw new BadRequestException(String.format(
                        "require a string but got %s", value.getClass()));
            }
        }
        if (type.equals(ColumnType.SELECT_ONE_NUMERIC)) {
            if (!(value instanceof Number)) {
                throw new BadRequestException(String.format(
                        "require a numeric but got %s", value.getClass()));
            }
        }
        if (type.equals(ColumnType.SELECT_MANY) || type.equals(ColumnType.SELECT_MANY_NUMERIC)) {
            if (!(value instanceof List)) {
                throw new BadRequestException(String.format(
                        "require a list but got %s", value.getClass()));
            }

            var list = (List<?>) value;
            if (list.isEmpty()) {
                throw new BadRequestException("require a sized list");
            }
            for (var e : list) {
                if (type.equals(ColumnType.SELECT_MANY) && !(e instanceof String)) {
                    throw new BadRequestException("require a string list");
                } else if (type.equals(ColumnType.SELECT_MANY_NUMERIC) && !(e instanceof Number)) {
                    throw new BadRequestException("require a numeric list");
                }
            }
        }
    }
}
