package org.dreamcat.anna.core.controller.table;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.config.AppConfig;
import org.dreamcat.anna.core.controller.table.query.CreateIndexQuery;
import org.dreamcat.anna.core.controller.table.query.DropIndexQuery;
import org.dreamcat.anna.core.service.TableIndexService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create by tuke on 2020/11/3
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/table/index")
public class TableIndexController {

    private final TableIndexService service;

    @RequestMapping(path = {"/", "/create"}, method = RequestMethod.POST)
    public RestBody<?> createIndex(@Valid @RequestBody CreateIndexQuery query) {
        return service.createIndex(query);
    }

    @RequestMapping(path = {"/", "/drop"}, method = RequestMethod.DELETE)
    public RestBody<?> dropIndex(@Valid @RequestBody DropIndexQuery query) {
        return service.dropIndex(query);
    }
}
