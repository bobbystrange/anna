package org.dreamcat.anna.relaxed.service.impl;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.Auth;
import org.dreamcat.anna.relaxed.controller.row.query.DeleteFromQuery;
import org.dreamcat.anna.relaxed.controller.row.query.InsertIntoOrUpdateSetQuery;
import org.dreamcat.anna.relaxed.controller.row.query.SelectFromQuery;
import org.dreamcat.anna.relaxed.controller.row.result.SelectFromResult;
import org.dreamcat.anna.relaxed.core.NameValuePair;
import org.dreamcat.anna.relaxed.core.ValueStrategy;
import org.dreamcat.anna.relaxed.dao.ColumnDefDao;
import org.dreamcat.anna.relaxed.dao.RowDataDao;
import org.dreamcat.anna.relaxed.dao.TableDefDao;
import org.dreamcat.anna.relaxed.entity.ColumnDefEntity;
import org.dreamcat.anna.relaxed.entity.RowDataEntity;
import org.dreamcat.anna.relaxed.service.RowService;
import org.dreamcat.anna.relaxed.service.RowValueService;
import org.dreamcat.common.web.core.RestBody;
import org.dreamcat.common.web.exception.BadRequestException;
import org.dreamcat.common.web.exception.InternalServerErrorException;
import org.dreamcat.common.web.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private final RowValueService rowValueService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> insertInto(InsertIntoOrUpdateSetQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
        }
        var tableId = table.getId();

        var queryColumnMap = query.getColumns().stream().collect(
                Collectors.toMap(InsertIntoOrUpdateSetQuery.Column::getName, Function.identity()));
        if (query.countColumnSize() < queryColumnMap.size()) {
            return RestBody.error("duplicate name in columns");
        }

        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);

        // primary column
        var primaryColumn = columns.stream()
                .filter(it -> Objects.equals(it.getPrimary(), true))
                .findFirst().orElse(null);
        if (primaryColumn == null) {
            throw new InternalServerErrorException(String.format("table '%s' has damaged", name));
        }

        var primaryColumnId = primaryColumn.getId();
        var primaryColumnName = primaryColumn.getName();
        var primaryQueryColumn = queryColumnMap.get(primaryColumnName);
        if (primaryQueryColumn == null) {
            throw new BadRequestException(String.format(
                    "primary column '%s' is required", primaryColumnName));
        }
        var primaryValueObject = primaryQueryColumn.getValue();
        if (!(primaryValueObject instanceof String) && !(primaryValueObject instanceof Number)) {
            throw new BadRequestException(String.format(
                    "primary column '%s' has wrong type (String/Number is required)",
                    primaryColumnName));
        }
        var primaryValue = primaryValueObject.toString();
        var rows = rowDataDao.findAllByTenantIdAndColumnIdAndPrimaryValue(
                tenantId, primaryColumnId, primaryValue);
        if (!rows.isEmpty()) {
            return RestBody.error("duplicate value in primary key column");
        }

        // normal column
        rows = new ArrayList<>(columns.size());
        var valueStrategy = new ValueStrategy();
        valueStrategy.setTenantId(tenantId);
        for (var column : columns) {
            var columnId = column.getId();
            var columnName = column.getName();
            var queryColumn = queryColumnMap.get(columnName);
            if (queryColumn == null) {
                if (Objects.equals(column.getRequired(), true)) {
                    return RestBody.error(String.format("column '%s' is required", columnName));
                }
                continue;
            }
            var value = queryColumn.getValue();

            var row = new RowDataEntity();
            rows.add(row);
            row.setTenantId(tenantId);
            row.setTableId(tableId);
            row.setColumnId(columnId);
            row.setPrimaryValue(primaryValue);

            var literal = rowValueService.formatToLiteral(
                    value, column.getType(), valueStrategy.atColumn(columnId));
            row.setValue(literal);
        }

        rowDataDao.saveAll(rows);
        return RestBody.ok();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> deleteFrom(DeleteFromQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
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
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
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
        for (var queryColumn : queryColumns) {
            var column = columnMap.get(queryColumn);
            if (column == null) {
                return RestBody.error(String.format("column '%s' not found", queryColumn));
            }

            var columnId = column.getId();
            var type = column.getType();
            var row = rowMap.get(columnId);
            if (row == null) {
                // the value is not stored
                list.add(new NameValuePair(queryColumn, null));
                continue;
            }

            var value = row.getValue();
            if (value == null) {
                throw new InternalServerErrorException(String.format(
                        "row '%s' in table '%s' has damaged", primaryValue, name));
            }
            try {
                list.add(new NameValuePair(queryColumn, rowValueService.parseLiteral(value, type)));
            } catch (Exception ignore) {
                throw new InternalServerErrorException(String.format(
                        "row '%s' in table '%s' has damaged", primaryValue, name));
            }
        }

        var result = new SelectFromResult();
        result.setColumns(list);
        return RestBody.ok(result);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> updateSet(InsertIntoOrUpdateSetQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getTable();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
        }
        var tableId = table.getId();

        var queryColumnMap = query.getColumnMap();
        if (query.countColumnSize() < queryColumnMap.size()) {
            return RestBody.error("duplicate name in columns");
        }

        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);
        // primary column
        var primaryColumn = columns.stream()
                .filter(ColumnDefEntity::getPrimary)
                .findFirst().orElse(null);
        if (primaryColumn == null) {
            throw new InternalServerErrorException(String.format("table '%s' has damaged", name));
        }

        var primaryColumnId = primaryColumn.getId();
        var primaryColumnName = primaryColumn.getName();
        var primaryQueryColumn = queryColumnMap.get(primaryColumnName);
        if (primaryQueryColumn == null) {
            throw new BadRequestException(String.format(
                    "primary column '%s' is required", primaryColumnName));
        }
        var primaryValueObject = primaryQueryColumn.getValue();
        if (!(primaryValueObject instanceof String) && !(primaryValueObject instanceof Number)) {
            return RestBody.error(String.format(
                    "primary column '%s' has wrong type (String/Number is required)",
                    primaryColumnName));
        }
        var primaryValue = primaryValueObject.toString();
        var rows = rowDataDao.findAllByTenantIdAndColumnIdAndPrimaryValue(tenantId, primaryColumnId,
                primaryValue);
        if (rows.isEmpty()) {
            return RestBody.error(String.format(
                    "primary key value '%s' in table '%s' is not found", name, primaryValue));
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
                throw new BadRequestException(String.format(
                        "column '%s' in table '%s' doesn't exist", columnName, table.getName()));
            }
            var value = queryColumn.getValue();

            var row = rowMap.get(columnId);
            if (row == null) {
                row = new RowDataEntity();
                row.setTenantId(tenantId);
                row.setTableId(tableId);
                row.setColumnId(columnId);
                row.setPrimaryValue(primaryValue);
            }

            var literal = rowValueService.formatToLiteral(
                    value, column.getType(), valueStrategy.atColumn(columnId));
            row.setValue(literal);
            saveRows.add(row);
        }

        // Note that only insert or update, not delete
        rowDataDao.saveAll(saveRows);
        return RestBody.ok();
    }

}
