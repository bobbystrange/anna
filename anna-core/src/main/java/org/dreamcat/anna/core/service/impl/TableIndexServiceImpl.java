package org.dreamcat.anna.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.controller.table.query.CreateIndexQuery;
import org.dreamcat.anna.core.controller.table.query.DropIndexQuery;
import org.dreamcat.anna.core.service.TableIndexService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.stereotype.Service;

/**
 * Create by tuke on 2020/11/3
 */
@RequiredArgsConstructor
@Service
public class TableIndexServiceImpl implements TableIndexService {

    @Override
    public RestBody<?> createIndex(CreateIndexQuery query) {
        return null;
    }

    @Override
    public RestBody<?> dropIndex(DropIndexQuery query) {
        return null;
    }
}
