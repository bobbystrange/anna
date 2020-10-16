package org.dreamcat.anna.relaxed.service;

import org.dreamcat.anna.relaxed.controller.view.query.CreateOrAlterViewQuery;
import org.dreamcat.anna.relaxed.controller.view.query.QueryViewQuery;
import org.dreamcat.anna.relaxed.controller.view.result.DescViewResult;
import org.dreamcat.anna.relaxed.controller.view.result.QueryViewResult;
import org.dreamcat.common.web.core.RestBody;

/**
 * Create by tuke on 2020/10/15
 */
public interface ViewService {

    RestBody<?> createView(CreateOrAlterViewQuery query);

    RestBody<?> dropView(String name);

    RestBody<DescViewResult> descView(String name);

    RestBody<?> alterView(CreateOrAlterViewQuery query);

    RestBody<QueryViewResult> queryView(QueryViewQuery query);
}
