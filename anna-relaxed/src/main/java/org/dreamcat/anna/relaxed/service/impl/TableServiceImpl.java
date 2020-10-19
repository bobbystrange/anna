package org.dreamcat.anna.relaxed.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.Auth;
import org.dreamcat.anna.relaxed.controller.table.query.CreateOrAlterTableQuery;
import org.dreamcat.anna.relaxed.controller.table.result.DescTableResult;
import org.dreamcat.anna.relaxed.dao.ColumnDefDao;
import org.dreamcat.anna.relaxed.dao.TableDefDao;
import org.dreamcat.anna.relaxed.entity.ColumnDefEntity;
import org.dreamcat.anna.relaxed.entity.TableDefEntity;
import org.dreamcat.anna.relaxed.service.TableService;
import org.dreamcat.common.web.core.RestBody;
import org.dreamcat.common.web.exception.BadRequestException;
import org.dreamcat.common.web.exception.NotFoundException;
import org.dreamcat.common.web.util.BeanCopierUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class TableServiceImpl implements TableService {
    // DAO
    private final TableDefDao tableDefDao;
    private final ColumnDefDao columnDefDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> createTable(CreateOrAlterTableQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getName();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table != null) {
            return RestBody.error(String.format("duplicate table name '%s'", name));
        }
        table = new TableDefEntity();
        table.setTenantId(tenantId);
        table.setName(query.getName());
        table.setDisplayName(query.getDisplayName());
        try {
            table = tableDefDao.save(table);
        } catch (Exception e) {
            // race condition for table name
            return RestBody.error(String.format("duplicate table name '%s'", name));
        }
        var tableId = table.getId();

        var queryColumns = query.getColumns();
        var queryColumnSize = queryColumns.size();
        if (query.countColumnSize() < queryColumnSize) {
            throw new BadRequestException("duplicate name in columns");
        }
        var columns = queryColumns.stream()
                .map(queryColumn -> {
                    var column = BeanCopierUtil.copy(queryColumn, ColumnDefEntity.class);
                    column.setTenantId(tenantId);
                    column.setTableId(tableId);
                    return column;
                })
                .collect(Collectors.toList());
        columnDefDao.saveAll(columns);
        return RestBody.OK;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> dropTable(String name) {
        var tenantId = Auth.getTenantId();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
        }
        var tableId = table.getId();

        tableDefDao.deleteById(tableId);
        return RestBody.ok();
    }

    @Override
    public RestBody<DescTableResult> descTable(String name) {
        var tenantId = Auth.getTenantId();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
        }
        var tableId = table.getId();

        var result = new DescTableResult();
        result.setName(name);
        result.setDisplayName(table.getDisplayName());

        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);
        result.setColumns(columns.stream()
                .map(it -> BeanCopierUtil.copy(it, DescTableResult.ColumnResult.class))
                .collect(Collectors.toList()));
        return RestBody.ok(result);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> alterTable(CreateOrAlterTableQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getName();
        var table = tableDefDao.findByTenantIdAndName(tenantId, name);
        if (table == null) {
            throw new NotFoundException(String.format("table '%s' doesn't exist", name));
        }
        var tableId = table.getId();

        // query
        var queryColumns = query.getColumns();
        var queryColumnSize = queryColumns.size();
        if (query.countColumnSize() < queryColumnSize) {
            throw new BadRequestException("duplicate name in columns");
        }
        var queryColumnMap = queryColumns.stream()
                .collect(Collectors.toMap(CreateOrAlterTableQuery.ColumnParam::getName, Function.identity()));
        // db
        var columns = columnDefDao.findAllByTenantIdAndTableId(tenantId, tableId);
        var columnMap = columns.stream()
                .collect(Collectors.toMap(ColumnDefEntity::getName, Function.identity()));

        // in query but not in db, then need to insert
        var insertNames = new HashSet<>(queryColumnMap.keySet());
        insertNames.removeAll(columnMap.keySet());
        // not in query but in db, then need to delete
        var deleteNames = new HashSet<>(columnMap.keySet());
        deleteNames.removeAll(queryColumnMap.keySet());
        // both in query and db, then need to update
        var updateNames = new HashSet<>(queryColumnMap.keySet());
        updateNames.removeAll(insertNames);

        if (!insertNames.isEmpty()) {
            var insertColumns = insertNames.stream()
                    .map(queryColumnMap::get)
                    .map(queryColumn -> {
                        var column = BeanCopierUtil.copy(queryColumn, ColumnDefEntity.class);
                        column.setTenantId(tenantId);
                        column.setTableId(tableId);
                        return column;
                    })
                    .collect(Collectors.toList());
            columnDefDao.saveAll(insertColumns);
        }

        if (!deleteNames.isEmpty()) {
            var deleteColumns = deleteNames.stream()
                    .map(columnMap::get)
                    .collect(Collectors.toList());
            columnDefDao.deleteInBatch(deleteColumns);
        }

        if (!updateNames.isEmpty()) {
            var updateColumns = updateNames.stream()
                    .map(updateName -> {
                        var column = columnMap.get(updateName);
                        var queryColumn = queryColumnMap.get(updateName);
                        BeanCopierUtil.copy(column, queryColumn);
                        return column;
                    })
                    .collect(Collectors.toList());
            columnDefDao.saveAll(updateColumns);
        }

        return RestBody.ok();
    }

}
