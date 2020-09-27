package org.dreamcat.anna.relaxed.controller.row;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.AppConfig;
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
@RequestMapping(path = AppConfig.API_VERSION + "/row")
public class RowController {
    private final RowService service;

    @RequestMapping(path = "insert", method = RequestMethod.POST)
    public RestBody<?> insertInto(@Valid @RequestBody InsertIntoOrUpdateSetQuery query) {
        return service.insertInto(query);
    }

    @RequestMapping(path = "delete", method = RequestMethod.POST)
    public RestBody<?> deleteFrom(@Valid @RequestBody DeleteFromQuery query) {
        return service.deleteFrom(query);
    }

    @RequestMapping(path = "select", method = RequestMethod.POST)
    public RestBody<SelectFromResult> selectFrom(@Valid @RequestBody SelectFromQuery query) {
        return service.selectFrom(query);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public RestBody<?> updateSet(@Valid @RequestBody InsertIntoOrUpdateSetQuery query) {
        return service.updateSet(query);
    }

}
