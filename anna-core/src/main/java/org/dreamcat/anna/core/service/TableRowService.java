package org.dreamcat.anna.core.service;

import org.dreamcat.anna.core.controller.table.query.DeleteFromQuery;
import org.dreamcat.anna.core.controller.table.query.InsertIntoQuery;
import org.dreamcat.anna.core.controller.table.query.SelectFromQuery;
import org.dreamcat.anna.core.controller.table.query.UpdateSetQuery;
import org.dreamcat.anna.core.controller.table.result.SelectFromResult;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/9/27
 */
public interface TableRowService {

    RestBody<?> insertInto(InsertIntoQuery query);

    RestBody<?> deleteFrom(DeleteFromQuery query);

    RestBody<SelectFromResult> selectFrom(SelectFromQuery query);

    RestBody<?> updateSet(UpdateSetQuery query);
}
