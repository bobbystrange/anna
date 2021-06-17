package org.dreamcat.anna.core.service;

import org.dreamcat.anna.core.controller.table.query.AlterTableQuery;
import org.dreamcat.anna.core.controller.table.query.CreateTableQuery;
import org.dreamcat.anna.core.controller.table.result.DescTableResult;

/**
 * Create by tuke on 2020/10/31
 */
public interface TableService {

    /**
     * create a table, only the metadata, no the physical table
     *
     * @param query query body
     * @return table id
     */
    Long createTable(CreateTableQuery query);

    /**
     * drop a table, include the physical table
     *
     * @param id drop by table id
     */
    void dropTable(Long id);

    /**
     * alter a table metadata
     *
     * @param query query body
     * @return table id
     */
    Long alterTable(AlterTableQuery query);

    /**
     * desc a table metadata
     *
     * @param id table id
     * @return table metadata
     */
    DescTableResult descTable(Long id);
}
