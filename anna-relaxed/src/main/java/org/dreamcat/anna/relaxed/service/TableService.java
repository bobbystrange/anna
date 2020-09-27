package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.controller.CreateTableQuery;
import org.dreamcat.anna.relaxed.controller.InsertIntoQuery;
import org.dreamcat.anna.relaxed.controller.SelectFromQuery;
import org.dreamcat.anna.relaxed.controller.SelectFromResult;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/9/14
 */
public interface TableService {

    RestBody<?> createTable(CreateTableQuery query);

    RestBody<?> insertInto(InsertIntoQuery query);

    RestBody<SelectFromResult> selectFrom(SelectFromQuery query);
}
