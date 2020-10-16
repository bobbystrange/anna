package org.dreamcat.anna.relaxed.controller.row;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.AppConfig;
import org.dreamcat.anna.relaxed.controller.row.query.DeleteFromQuery;
import org.dreamcat.anna.relaxed.controller.row.query.InsertIntoOrUpdateSetQuery;
import org.dreamcat.anna.relaxed.controller.row.query.SelectFromQuery;
import org.dreamcat.anna.relaxed.controller.row.result.SelectFromResult;
import org.dreamcat.anna.relaxed.service.RowService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create by tuke on 2020/9/27
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/row", method = RequestMethod.POST)
public class RowController {
    private final RowService service;

    @RequestMapping(path = "insert")
    public RestBody<?> insertInto(@Valid @RequestBody InsertIntoOrUpdateSetQuery query) {
        return service.insertInto(query);
    }

    @RequestMapping(path = "delete")
    public RestBody<?> deleteFrom(@Valid @RequestBody DeleteFromQuery query) {
        return service.deleteFrom(query);
    }

    @RequestMapping(path = "select")
    public RestBody<SelectFromResult> selectFrom(@Valid @RequestBody SelectFromQuery query) {
        return service.selectFrom(query);
    }

    @RequestMapping(path = "update")
    public RestBody<?> updateSet(@Valid @RequestBody InsertIntoOrUpdateSetQuery query) {
        return service.updateSet(query);
    }

}
