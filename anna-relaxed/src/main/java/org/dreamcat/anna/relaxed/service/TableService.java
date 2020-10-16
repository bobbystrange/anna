package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.controller.table.query.CreateOrAlterTableQuery;
import org.dreamcat.anna.relaxed.controller.table.result.DescTableResult;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/9/14
 */
public interface TableService {

    RestBody<?> createTable(CreateOrAlterTableQuery query);

    RestBody<?> dropTable(String name);

    RestBody<DescTableResult> descTable(String name);

    RestBody<?> alterTable(CreateOrAlterTableQuery query);
}
