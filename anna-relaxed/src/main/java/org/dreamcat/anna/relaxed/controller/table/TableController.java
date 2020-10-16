package org.dreamcat.anna.relaxed.controller.table;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.AppConfig;
import org.dreamcat.anna.relaxed.controller.table.query.CreateOrAlterTableQuery;
import org.dreamcat.anna.relaxed.controller.table.result.DescTableResult;
import org.dreamcat.anna.relaxed.service.TableService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create by tuke on 2020/9/12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/table")
public class TableController {
    private final TableService service;

    @RequestMapping(method = RequestMethod.POST)
    public RestBody<?> createTable(@Valid @RequestBody CreateOrAlterTableQuery query) {
        return service.createTable(query);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public RestBody<?> dropTable(@RequestParam(name = "name") String name) {
        return service.dropTable(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public RestBody<DescTableResult> descTable(@RequestParam(name = "name") String name) {
        return service.descTable(name);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public RestBody<?> alterTable(@Valid @RequestBody CreateOrAlterTableQuery query) {
        return service.alterTable(query);
    }

}
