package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.controller.view.query.CreateOrAlterViewQuery;
import org.dreamcat.anna.relaxed.controller.view.query.QueryViewQuery;
import org.dreamcat.anna.relaxed.controller.view.result.DescViewResult;
import org.dreamcat.anna.relaxed.core.NameValuePair;
import org.dreamcat.common.web.core.RestBody;

import java.util.List;
import java.util.Map;

/**
 * Create by tuke on 2020/10/15
 */
public interface ViewService {

    RestBody<?> createView(CreateOrAlterViewQuery query);

    RestBody<?> dropView(String name);

    RestBody<DescViewResult> descView(String name);

    RestBody<?> alterView(CreateOrAlterViewQuery query);

    RestBody<List<NameValuePair>> queryView(QueryViewQuery query);

    RestBody<Map<String, ?>> queryViewAsExampleMap(QueryViewQuery query);

    RestBody<Map<String, ?>> queryViewAsMap(QueryViewQuery query);
}
