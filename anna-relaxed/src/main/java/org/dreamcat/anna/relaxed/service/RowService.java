package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.controller.row.DeleteFromQuery;
import org.dreamcat.anna.relaxed.controller.row.InsertIntoOrUpdateSetQuery;
import org.dreamcat.anna.relaxed.controller.row.SelectFromQuery;
import org.dreamcat.anna.relaxed.controller.row.SelectFromResult;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/9/27
 */
public interface RowService {

    RestBody<?> insertInto(InsertIntoOrUpdateSetQuery query);

    RestBody<?> deleteFrom(DeleteFromQuery query);

    RestBody<SelectFromResult> selectFrom(SelectFromQuery query);

    RestBody<?> updateSet(InsertIntoOrUpdateSetQuery query);
}
