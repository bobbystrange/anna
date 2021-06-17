package org.dreamcat.anna.core.service;

import org.dreamcat.anna.core.controller.table.query.CreateIndexQuery;
import org.dreamcat.anna.core.controller.table.query.DropIndexQuery;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/11/3
 */
public interface TableIndexService {

    RestBody<?> createIndex(CreateIndexQuery query);

    RestBody<?> dropIndex(DropIndexQuery query);
}
