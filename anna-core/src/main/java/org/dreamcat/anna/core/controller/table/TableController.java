package org.dreamcat.anna.core.controller.table;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.config.AppConfig;
import org.dreamcat.anna.core.controller.table.query.AlterTableQuery;
import org.dreamcat.anna.core.controller.table.query.CreateTableQuery;
import org.dreamcat.anna.core.controller.table.result.DescTableResult;
import org.dreamcat.anna.core.service.TableService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Create by tuke on 2020/9/12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/table")
public class TableController {

    private final TableService service;

    @RequestMapping(method = RequestMethod.POST)
    public Long createTable(@Valid @RequestBody CreateTableQuery query) {
        return service.createTable(query);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Long alterTable(@Valid @RequestBody AlterTableQuery query) {
        return service.alterTable(query);
    }

    @RequestMapping(method = RequestMethod.GET)
    public DescTableResult descTable(@RequestParam(name = "id") Long id) {
        return service.descTable(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void dropTable(@RequestParam(name = "id") Long id) {
        service.dropTable(id);
    }
}
