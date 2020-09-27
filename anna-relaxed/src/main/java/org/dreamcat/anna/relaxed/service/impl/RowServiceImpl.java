package org.dreamcat.anna.relaxed.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.Auth;
import org.dreamcat.anna.relaxed.controller.row.DeleteFromQuery;
import org.dreamcat.anna.relaxed.controller.row.InsertIntoOrUpdateSetQuery;
import org.dreamcat.anna.relaxed.controller.row.SelectFromQuery;
import org.dreamcat.anna.relaxed.controller.row.SelectFromResult;
import org.dreamcat.anna.relaxed.core.NameValuePair;
import org.dreamcat.anna.relaxed.core.ValueStrategy;
import org.dreamcat.anna.relaxed.dao.ColumnDefDao;
import org.dreamcat.anna.relaxed.dao.RowDataDao;
import org.dreamcat.anna.relaxed.dao.TableDefDao;
import org.dreamcat.anna.relaxed.entity.ColumnDefEntity;
import org.dreamcat.anna.relaxed.entity.RowDataEntity;
import org.dreamcat.anna.relaxed.entity.TableDefEntity;
import org.dreamcat.anna.relaxed.entity.strategy.ColumnType;
import org.dreamcat.anna.relaxed.service.ReachabilityService;
import org.dreamcat.anna.relaxed.service.RowService;
import org.dreamcat.anna.relaxed.service.RowValueService;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.web.core.RestBody;
import org.dreamcat.common.web.exception.BadRequestException;
import org.dreamcat.common.web.exception.InternalServerErrorException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/27
 */
@RequiredArgsConstructor
@Service
public class RowServiceImpl implements RowService {
    /// DAO
    private final TableDefDao tableDefDao;
    private final ColumnDefDao columnDefDao;
    private final RowDataDao rowDataDao;
    /// Service
    private final ReachabilityService reachabilityService;
    private final RowValueService rowValueService;

    @Override
    public RestBody<?> insertInto(InsertIntoOrUpdateSetQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            return RestBody.error("table not found");
        }
        var tableId = table.getId();

        var queryColumnMap = query.getColumns().stream()
                .collect(Collectors.toMap(InsertIntoOrUpdateSetQuery.ColumnParam::getName, Function.identity()));
        if (query.countColumnSize() < queryColumnMap.size()) {
            return RestBody.error("duplicate name in columns");
        }

        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);

        // primary column
        var primaryColumn = columns.stream().filter(it -> Objects.equals(it.isPrimary(), true)).findFirst().orElse(null);
        if (primaryColumn == null) {
            throw new InternalServerErrorException(String.format("table '%s' has damaged", table.getName()));
        }

        var columnId = primaryColumn.getId();
        var primaryColumnName = primaryColumn.getName();
        var primaryQueryColumn = queryColumnMap.get(primaryColumnName);
        if (primaryQueryColumn == null) {
            throw new BadRequestException(String.format("primary column '%s' is required", primaryColumnName));
        }
        var primaryValueObject = primaryQueryColumn.getValue();
        if (!(primaryValueObject instanceof String) && !(primaryValueObject instanceof Number)) {
            return RestBody.error(String.format("primary column '%s' has wrong type (String/Number is required)", primaryColumnName));
        }
        var primaryValue = primaryValueObject.toString();
        var rows = rowDataDao.findAllByTenantIdAndColumnIdAndPrimaryValue(tenantId, columnId, primaryValue);
        if (!rows.isEmpty()) {
            return RestBody.error("duplicate value in primary key column");
        }

        // normal column
        rows = new ArrayList<>(columns.size());
        var valueStrategy = new ValueStrategy();
        valueStrategy.setTenantId(tenantId);
        for (var column : columns) {
            var columnName = column.getName();
            var queryColumn = queryColumnMap.get(columnName);
            if (queryColumn == null) {
                if (Objects.equals(column.isRequired(), true)) {
                    return RestBody.error(String.format("column '%s' is required", columnName));
                }
                continue;
            }
            var value = queryColumn.getValue();

            var row = new RowDataEntity();
            rows.add(row);
            row.setTenantId(tenantId);
            row.setTableId(tableId);
            row.setColumnId(column.getId());
            row.setPrimaryValue(primaryValue);

            var pair = formatToLiteral(table, column, value, valueStrategy);
            var pairError = pair.second();
            if (pairError != null) return pairError;
            var literal = pair.first();
            row.setValue(literal);
        }

        rowDataDao.saveAll(rows);
        return RestBody.ok();
    }

    @Override
    public RestBody<?> deleteFrom(DeleteFromQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            return RestBody.error("table not found");
        }
        var tableId = table.getId();
        tableDefDao.deleteById(tableId);
        return RestBody.ok();
    }

    @Override
    public RestBody<SelectFromResult> selectFrom(SelectFromQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var primaryValue = query.getValue();
        var queryColumns = query.getColumns();

        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            return RestBody.error("table not found");
        }
        var tableId = table.getId();
        var columnMap = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId).stream()
                .collect(Collectors.toMap(ColumnDefEntity::getName, Function.identity()));
        if (queryColumns == null) {
            queryColumns = new ArrayList<>(columnMap.keySet());
        }

        var rowMap = rowDataDao.findAllByTenantIdAndPrimaryValue(tenantId, primaryValue).stream()
                .collect(Collectors.toMap(RowDataEntity::getColumnId, Function.identity()));
        var list = new ArrayList<NameValuePair>(queryColumns.size());

        int sourceSize = (int) columnMap.values().stream()
                .filter(it -> it.getType().equals(ColumnType.SOURCE))
                .count();
        var sourceNames = new ArrayList<String>(sourceSize);
        var sources = new ArrayList<String>(sourceSize);
        for (var queryColumn : queryColumns) {
            var column = columnMap.get(queryColumn);
            if (column == null) {
                return RestBody.error(String.format("column '%s' not found", queryColumn));
            }

            var columnId = column.getId();
            var type = column.getType();
            var row = rowMap.get(columnId);

            String source;
            if (!type.equals(ColumnType.SOURCE)) {
                if (row == null) {
                    // the value is not stored
                    list.add(new NameValuePair(queryColumn, null));
                    continue;
                }

                var value = row.getValue();
                if (value == null) {
                    throw new InternalServerErrorException(String.format("row '%s' in table '%s' has damaged", primaryValue, name));
                }
                try {
                    list.add(new NameValuePair(queryColumn, rowValueService.parseLiteral(value, type)));
                } catch (Exception ignore) {
                    throw new InternalServerErrorException(String.format("row '%s' in table '%s' has damaged", primaryValue, name));
                }
                continue;
            }

            // then type equals SOURCE
            source = column.getSource();
            if (source == null) {
                // the expression is not stored
                list.add(new NameValuePair(queryColumn, null));
                continue;
            }
            // source field case
            sourceNames.add(queryColumn);
            sources.add(source);
            // put a placeholder
            list.add(new NameValuePair(queryColumn, null));
        }

        if (!sources.isEmpty()) {
            var values = reachabilityService.parse(sources, table.getSourceTable(), table.getSourceColumn(), primaryValue);
            for (var pair : list) {
                var index = sourceNames.indexOf(pair.getName());
                if (index != -1) {
                    var value = values.get(index);
                    pair.setValue(value);
                }
            }
        }

        var result = new SelectFromResult();
        result.setColumns(list);
        return RestBody.ok(result);
    }

    @Override
    public RestBody<?> updateSet(InsertIntoOrUpdateSetQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            return RestBody.error("table not found");
        }
        var tableId = table.getId();

        var queryColumnMap = query.getColumns().stream()
                .collect(Collectors.toMap(InsertIntoOrUpdateSetQuery.ColumnParam::getName, Function.identity()));
        if (query.countColumnSize() < queryColumnMap.size()) {
            return RestBody.error("duplicate name in columns");
        }

        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);
        // primary column
        var primaryColumn = columns.stream().filter(it -> Objects.equals(it.isPrimary(), true)).findFirst().orElse(null);
        if (primaryColumn == null) {
            throw new InternalServerErrorException(String.format("table '%s' has damaged", table.getName()));
        }

        var primaryColumnId = primaryColumn.getId();
        var primaryColumnName = primaryColumn.getName();
        var primaryQueryColumn = queryColumnMap.get(primaryColumnName);
        if (primaryQueryColumn == null) {
            throw new BadRequestException(String.format("primary column '%s' is required", primaryColumnName));
        }
        var primaryValueObject = primaryQueryColumn.getValue();
        if (!(primaryValueObject instanceof String) && !(primaryValueObject instanceof Number)) {
            return RestBody.error(String.format("primary column '%s' has wrong type (String/Number is required)", primaryColumnName));
        }
        var primaryValue = primaryValueObject.toString();
        var rows = rowDataDao.findAllByTenantIdAndColumnIdAndPrimaryValue(tenantId, primaryColumnId, primaryValue);
        if (rows.isEmpty()) {
            return RestBody.error(String.format("primary key value '%s' in table '%s' is not found", table.getName(), primaryValue));
        }

        var rowMap = rows.stream()
                .collect(Collectors.toMap(RowDataEntity::getColumnId, Function.identity()));
        var valueStrategy = new ValueStrategy();
        valueStrategy.setTenantId(tenantId);
        var saveRows = new ArrayList<RowDataEntity>();
        for (var column : columns) {
            var columnId = column.getId();
            var columnName = column.getName();
            var queryColumn = queryColumnMap.get(columnName);
            if (queryColumn == null) {
                throw new BadRequestException(String.format("column '%s' in table '%s' doesn't exist", columnName, table.getName()));
            }
            var value = queryColumn.getValue();

            var pair = formatToLiteral(table, column, value, valueStrategy);
            var pairError = pair.second();
            if (pairError != null) return pairError;
            var literal = pair.first();

            var row = rowMap.get(columnId);
            if (row == null) {
                row = new RowDataEntity();
                row.setTenantId(tenantId);
                row.setTableId(tableId);
                row.setColumnId(columnId);
                row.setPrimaryValue(primaryValue);
            }
            row.setValue(literal);
            saveRows.add(row);
        }

        // Note that only insert or update, not delete
        rowDataDao.saveAll(saveRows);
        return RestBody.ok();
    }

    private Pair<String, RestBody<?>> formatToLiteral(TableDefEntity table, ColumnDefEntity column, Object value, ValueStrategy valueStrategy) {
        var type = column.getType();
        if (type.equals(ColumnType.SOURCE)) {
            if (!(value instanceof String)) {
                return Pair.of(null, RestBody.error(String.format("source column '%s' has wrong type (String is required)", column.getName())));
            }
            var source = (String) value;
            if (!reachabilityService.test(source, table.getSourceTable())) {
                return Pair.of(null, RestBody.error(String.format("source column '%s' has a unreached source", column.getName())));
            }
            return Pair.of(source, null);
        }

        var literal = rowValueService.formatToLiteral(value, type, valueStrategy.atColumn(column.getId()));
        return Pair.of(literal, null);
    }
}
