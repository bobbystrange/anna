package org.dreamcat.anna.core.controller.table;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.config.AppConfig;
import org.dreamcat.anna.core.controller.table.query.AddColumnQuery;
import org.dreamcat.anna.core.controller.table.query.ModifyColumnQuery;
import org.dreamcat.anna.core.controller.table.result.DescColumnResult;
import org.dreamcat.anna.core.service.TableColumnService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by tuke on 2021/1/29
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/table/column")
public class TableColumnController {

    private final TableColumnService service;

    @RequestMapping(method = RequestMethod.POST)
    public Long addColumn(@Valid @RequestBody AddColumnQuery query) {
        return service.addColumn(query);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Long modifyColumn(@Valid @RequestBody ModifyColumnQuery query) {
        return service.modifyColumn(query);
    }

    @RequestMapping(method = RequestMethod.GET)
    public DescColumnResult descColumn(@RequestParam("id") Long id) {
        return service.descColumn(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void dropColumn(@RequestParam("id") Long id) {
        service.dropColumn(id);
    }
}
