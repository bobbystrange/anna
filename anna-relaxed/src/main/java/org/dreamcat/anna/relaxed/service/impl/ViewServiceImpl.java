package org.dreamcat.anna.relaxed.service.impl;

import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.relaxed.config.Auth;
import org.dreamcat.anna.relaxed.controller.view.query.CreateOrAlterViewQuery;
import org.dreamcat.anna.relaxed.controller.view.query.QueryViewQuery;
import org.dreamcat.anna.relaxed.controller.view.result.DescViewResult;
import org.dreamcat.anna.relaxed.core.NameValuePair;
import org.dreamcat.anna.relaxed.core.condition.ConditionArgContext;
import org.dreamcat.anna.relaxed.dao.ViewDefDao;
import org.dreamcat.anna.relaxed.dao.ViewFieldDefDao;
import org.dreamcat.anna.relaxed.entity.ViewDefEntity;
import org.dreamcat.anna.relaxed.entity.ViewFieldDefEntity;
import org.dreamcat.anna.relaxed.service.ReachabilityService;
import org.dreamcat.anna.relaxed.service.ViewService;
import org.dreamcat.common.function.ObjectArrayFunction;
import org.dreamcat.common.util.CollectionUtil;
import org.dreamcat.common.web.core.RestBody;
import org.dreamcat.common.web.exception.BadRequestException;
import org.dreamcat.common.web.exception.NotFoundException;
import org.dreamcat.common.web.util.BeanCopierUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/10/15
 */
@RequiredArgsConstructor
@Service
public class ViewServiceImpl implements ViewService {
    // DAO
    private final ViewDefDao viewDefDao;
    private final ViewFieldDefDao viewFieldDefDao;
    /// Service
    private final ReachabilityService reachabilityService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> createView(CreateOrAlterViewQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getName();
        var view = viewDefDao.findByTenantIdAndName(tenantId, name);
        if (view != null) {
            return RestBody.error(String.format("duplicate view name '%s'", name));
        }
        view = new ViewDefEntity();
        view.setTenantId(tenantId);
        view.setName(query.getName());
        view.setDisplayName(query.getDisplayName());
        try {
            view = viewDefDao.save(view);
        } catch (Exception e) {
            // race condition for view name
            return RestBody.error(String.format("duplicate view name '%s'", name));
        }
        var viewId = view.getId();

        var queryFields = query.getFields();
        var queryFieldSize = queryFields.size();
        if (query.countFieldSize() < queryFieldSize) {
            throw new BadRequestException("duplicate name in fields");
        }
        var columns = queryFields.stream()
                .map(queryField -> {
                    var column = BeanCopierUtil.copy(queryField, ViewFieldDefEntity.class);
                    column.setTenantId(tenantId);
                    column.setViewId(viewId);
                    return column;
                })
                .collect(Collectors.toList());
        viewFieldDefDao.saveAll(columns);
        return RestBody.OK;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> dropView(String name) {
        var tenantId = Auth.getTenantId();
        var view = viewDefDao.findByTenantIdAndName(tenantId, name);
        if (view == null) {
            return RestBody.error(String.format("view '%s' doesn't exist", name));
        }
        var viewId = view.getId();
        viewDefDao.deleteById(viewId);
        return RestBody.ok();
    }

    @Override
    public RestBody<DescViewResult> descView(String name) {
        var tenantId = Auth.getTenantId();
        var view = viewDefDao.findByTenantIdAndName(tenantId, name);
        if (view == null) {
            return RestBody.error(String.format("view '%s' doesn't exist", name));
        }
        var viewId = view.getId();

        var result = new DescViewResult();
        result.setName(name);
        result.setDisplayName(view.getDisplayName());

        var fields = viewFieldDefDao.findAllByTenantIdAndViewId(tenantId, viewId);
        result.setFields(fields.stream().map(it -> BeanCopierUtil.copy(
                it, DescViewResult.FieldResult.class)).collect(Collectors.toList()));
        return RestBody.ok(result);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public RestBody<?> alterView(CreateOrAlterViewQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getName();
        var view = viewDefDao.findByTenantIdAndName(tenantId, name);
        if (view == null) {
            return RestBody.error(String.format("view '%s' doesn't exist", name));
        }
        var viewId = view.getId();

        // query
        var queryFields = query.getFields();
        var queryFieldSize = queryFields.size();
        if (query.countFieldSize() < queryFieldSize) {
            throw new BadRequestException("duplicate name in fields");
        }
        var queryFieldMap = queryFields.stream()
                .collect(Collectors.toMap(CreateOrAlterViewQuery.FieldParam::getName, Function.identity()));
        // db
        var fields = viewFieldDefDao.findAllByTenantIdAndViewId(tenantId, viewId);
        var fieldMap = fields.stream()
                .collect(Collectors.toMap(ViewFieldDefEntity::getName, Function.identity()));

        // in query but not in db, then need to insert
        var insertNames = new HashSet<>(queryFieldMap.keySet());
        insertNames.removeAll(fieldMap.keySet());
        // not in query but in db, then need to delete
        var deleteNames = new HashSet<>(fieldMap.keySet());
        deleteNames.removeAll(queryFieldMap.keySet());
        // both in query and db, then need to update
        var updateNames = new HashSet<>(queryFieldMap.keySet());
        updateNames.removeAll(insertNames);

        if (!insertNames.isEmpty()) {
            var insertColumns = insertNames.stream()
                    .map(queryFieldMap::get)
                    .map(queryColumn -> {
                        var field = BeanCopierUtil.copy(queryColumn, ViewFieldDefEntity.class);
                        field.setTenantId(tenantId);
                        field.setViewId(viewId);
                        return field;
                    })
                    .collect(Collectors.toList());
            viewFieldDefDao.saveAll(insertColumns);
        }

        if (!deleteNames.isEmpty()) {
            var deleteColumns = deleteNames.stream()
                    .map(fieldMap::get)
                    .collect(Collectors.toList());
            viewFieldDefDao.deleteInBatch(deleteColumns);
        }

        if (!updateNames.isEmpty()) {
            var updateColumns = updateNames.stream()
                    .map(updateName -> {
                        var filed = fieldMap.get(updateName);
                        var queryField = queryFieldMap.get(updateName);
                        BeanCopierUtil.copy(filed, queryField);
                        return filed;
                    })
                    .collect(Collectors.toList());
            viewFieldDefDao.saveAll(updateColumns);
        }

        return RestBody.ok();
    }

    @Override
    public RestBody<List<NameValuePair>> queryView(QueryViewQuery query) {
        var tenantId = Auth.getTenantId();
        var name = query.getView();
        var queryFieldList = query.getFields();

        var view = findView(name, tenantId);
        var viewId = view.getId();

        var result = new ArrayList<NameValuePair>();
        var expressions = extractExpressions(queryFieldList, viewId, tenantId, result);
        var context = new ConditionArgContext();
        context.setConditionArgs(query.getConditionArgs());
        var values = reachabilityService.parse(expressions, view.getSourceTable(), view.getSourceColumn(), query.getValue(), context);
        for (int i = 0, len = result.size(); i < len; i++) {
            result.get(i).setValue(CollectionUtil.elementAt(values, i));
        }
        return RestBody.ok(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RestBody<Map<String, ?>> queryViewAsExampleMap(QueryViewQuery query) {
        return queryViewAsMap(query, args -> reachabilityService.parseAsExampleMap(
                (List<String>) args[0], (String) args[1], (String) args[2], (String) args[3], (ConditionArgContext) args[4]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public RestBody<Map<String, ?>> queryViewAsMap(QueryViewQuery query) {
        return queryViewAsMap(query, args -> reachabilityService.parseAsMap(
                (List<String>) args[0], (String) args[1], (String) args[2], (String) args[3], (ConditionArgContext) args[4]));
    }

    private RestBody<Map<String, ?>> queryViewAsMap(QueryViewQuery query, ObjectArrayFunction<Map<String, ?>> parser) {
        var tenantId = Auth.getTenantId();
        var name = query.getView();
        var queryFieldList = query.getFields();

        var view = findView(name, tenantId);
        var viewId = view.getId();

        var expressions = extractExpressions(queryFieldList, viewId, tenantId, null);
        var context = new ConditionArgContext();
        context.setConditionArgs(query.getConditionArgs());
        var result = parser.apply(expressions, view.getSourceTable(), view.getSourceColumn(), query.getValue(), context);
        return RestBody.ok(result);
    }

    private ViewDefEntity findView(String name, Long tenantId) {
        var view = viewDefDao.findByTenantIdAndName(tenantId, name);
        if (view == null) {
            throw new NotFoundException(String.format("view '%s' doesn't exist", name));
        }
        return view;
    }

    private List<String> extractExpressions(List<String> queryFieldList, Long viewId, Long tenantId, ArrayList<NameValuePair> list) {
        var fields = viewFieldDefDao.findAllByTenantIdAndViewId(tenantId, viewId);
        Set<String> queryFields = Collections.emptySet();
        if (queryFieldList != null) {
            queryFields = new HashSet<>(queryFieldList);
            if (queryFields.size() < queryFieldList.size()) {
                throw new BadRequestException("duplicate value in fields");
            }
        }

        var queryFieldNotEmpty = !queryFields.isEmpty();
        var size = queryFieldNotEmpty ? queryFields.size() : fields.size();
        var expressions = new ArrayList<String>(size);
        if (list != null) list.ensureCapacity(size);
        for (var field : fields) {
            var fieldName = field.getName();
            if (queryFieldNotEmpty && queryFields.contains(fieldName)) continue;
            if (list != null) list.add(new NameValuePair(fieldName));
            expressions.add(field.getExpression());
        }
        return expressions;
    }
}
