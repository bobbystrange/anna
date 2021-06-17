package org.dreamcat.anna.core.service;

import org.dreamcat.anna.core.controller.table.query.AddColumnQuery;
import org.dreamcat.anna.core.controller.table.query.ModifyColumnQuery;
import org.dreamcat.anna.core.controller.table.result.DescColumnResult;

/**
 * Create by tuke on 2021/1/29
 */
public interface TableColumnService {

    Long addColumn(AddColumnQuery query);

    Long modifyColumn(ModifyColumnQuery query);

    void dropColumn(Long id);

    DescColumnResult descColumn(Long id);
}
