package org.dreamcat.anna.relaxed.controller;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.service.TableService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create by tuke on 2020/9/12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/metadata/def/table")
public class TableController {
    private final TableService service;

    @RequestMapping(method = RequestMethod.POST)
    public RestBody<?> createTable(@Valid @RequestBody CreateTableQuery query) {
        return service.createTable(query);
    }

    @RequestMapping(path = "/select", method = RequestMethod.POST)
    public RestBody<SelectFromResult> selectFrom(@Valid @RequestBody SelectFromQuery query) {
        return service.selectFrom(query);
    }

    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    public RestBody<?> insertInto(@Valid @RequestBody InsertIntoQuery query) {
        return service.insertInto(query);
    }

}
