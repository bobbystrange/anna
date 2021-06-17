package org.dreamcat.anna.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.controller.table.query.DeleteFromQuery;
import org.dreamcat.anna.core.controller.table.query.InsertIntoQuery;
import org.dreamcat.anna.core.controller.table.query.SelectFromQuery;
import org.dreamcat.anna.core.controller.table.query.UpdateSetQuery;
import org.dreamcat.anna.core.controller.table.result.SelectFromResult;
import org.dreamcat.anna.core.service.TableRowService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.stereotype.Service;

/**
 * Create by tuke on 2020/9/27
 */
@RequiredArgsConstructor
@Service
public class TableRowServiceImpl implements TableRowService {
    @Override
    public RestBody<?> insertInto(InsertIntoQuery query) {
        return null;
    }

    @Override
    public RestBody<?> deleteFrom(DeleteFromQuery query) {
        return null;
    }

    @Override
    public RestBody<SelectFromResult> selectFrom(SelectFromQuery query) {
        return null;
    }

    @Override
    public RestBody<?> updateSet(UpdateSetQuery query) {
        return null;
    }
}
