package org.dreamcat.anna.core.controller.table;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.config.AppConfig;
import org.dreamcat.anna.core.controller.table.query.DeleteFromQuery;
import org.dreamcat.anna.core.controller.table.query.InsertIntoQuery;
import org.dreamcat.anna.core.controller.table.query.SelectFromQuery;
import org.dreamcat.anna.core.controller.table.query.UpdateSetQuery;
import org.dreamcat.anna.core.controller.table.result.SelectFromResult;
import org.dreamcat.anna.core.service.TableRowService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by tuke on 2020/9/27
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/table/row", method = RequestMethod.POST)
public class TableRowController {

    private final TableRowService service;

    @RequestMapping(path = {"/", "/insert"}, method = RequestMethod.POST)
    public RestBody<?> insertInto(@Valid @RequestBody InsertIntoQuery query) {
        return service.insertInto(query);
    }

    @RequestMapping(path = {"/", "delete"}, method = RequestMethod.DELETE)
    public RestBody<?> deleteFrom(@Valid @RequestBody DeleteFromQuery query) {
        return service.deleteFrom(query);
    }

    @RequestMapping(path = {"/", "select"}, method = RequestMethod.GET)
    public RestBody<SelectFromResult> selectFrom(@Valid @RequestBody SelectFromQuery query) {
        return service.selectFrom(query);
    }

    @RequestMapping(path = {"/", "update"}, method = RequestMethod.PUT)
    public RestBody<?> updateSet(@Valid @RequestBody UpdateSetQuery query) {
        return service.updateSet(query);
    }

}
