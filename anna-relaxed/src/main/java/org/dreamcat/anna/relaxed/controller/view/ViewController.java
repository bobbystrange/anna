package org.dreamcat.anna.relaxed.controller.view;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.AppConfig;
import org.dreamcat.anna.relaxed.controller.view.query.CreateOrAlterViewQuery;
import org.dreamcat.anna.relaxed.controller.view.query.QueryViewQuery;
import org.dreamcat.anna.relaxed.controller.view.result.DescViewResult;
import org.dreamcat.anna.relaxed.common.NameValuePair;
import org.dreamcat.anna.relaxed.service.ViewService;
import org.dreamcat.common.web.core.RestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by tuke on 2020/10/15
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = AppConfig.API_VERSION + "/view")
public class ViewController {

    private final ViewService service;

    @RequestMapping(method = RequestMethod.POST)
    public RestBody<?> createView(@Valid @RequestBody CreateOrAlterViewQuery query) {
        return service.createView(query);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public RestBody<?> dropView(@RequestParam(name = "name") String name) {
        return service.dropView(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public RestBody<DescViewResult> descView(@RequestParam(name = "name") String name) {
        return service.descView(name);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public RestBody<?> alterView(@Valid @RequestBody CreateOrAlterViewQuery query) {
        return service.alterView(query);
    }

    @RequestMapping(path = "/query", method = RequestMethod.POST)
    public RestBody<List<NameValuePair>> queryView(@Valid @RequestBody QueryViewQuery query) {
        return service.queryView(query);
    }

    @RequestMapping(path = "/query/map", method = RequestMethod.POST)
    public RestBody<Map<String, ?>> queryViewAsMap(@Valid @RequestBody QueryViewQuery query) {
        return service.queryViewAsMap(query);
    }
}
