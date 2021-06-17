package org.dreamcat.anna.core.service.impl;

import static org.dreamcat.anna.core.config.i18n.TableMessageCode.TABLE_ALREADY_EXISTS;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dreamcat.anna.core.component.basic.CommonService;
import org.dreamcat.anna.core.controller.table.query.AlterTableQuery;
import org.dreamcat.anna.core.controller.table.query.CreateTableQuery;
import org.dreamcat.anna.core.controller.table.result.DescTableResult;
import org.dreamcat.anna.core.dao.TableDefDao;
import org.dreamcat.anna.core.entity.TableDefEntity;
import org.dreamcat.anna.core.service.TableService;
import org.dreamcat.common.web.exception.ForbiddenMessageException;
import org.dreamcat.common.x.asm.BeanCopierUtil;
import org.springframework.stereotype.Service;

/**
 * Create by tuke on 2020/9/14
 */
@RequiredArgsConstructor
@Service
public class TableServiceImpl implements TableService {

    /// Dao
    @Resource
    private TableDefDao tableDefDao;
    /// Service
    @Resource
    private CommonService commonService;

    @Transactional
    @Override
    public Long createTable(CreateTableQuery query) {
        var authInfo = commonService.getAuthInfo();
        var tenantId = authInfo.getTenantId();
        var name = query.getName();

        var table = tableDefDao.findByNameAndTenantId(name, tenantId);
        if (table != null) {
            throw new ForbiddenMessageException(TABLE_ALREADY_EXISTS, name);
        }

        table = commonService.newEntity(authInfo, TableDefEntity::new);
        BeanCopierUtil.copy(query, table);
        var savedTable = tableDefDao.save(table);

        // return the saved table id
        return savedTable.getId();
    }

    @Transactional
    @Override
    public void dropTable(Long id) {

    }

    @Transactional
    @Override
    public Long alterTable(AlterTableQuery query) {
        var authInfo = commonService.getAuthInfo();
        var tenantId = authInfo.getTenantId();
        var tableId = query.getTableId();

        var table = tableDefDao.fetchTable(tableId, tenantId);
        table.setAlias(query.getAlias());
        table.setComment(query.getComment());
        tableDefDao.save(table);

        // just return the table id
        return tableId;
    }

    @Override
    public DescTableResult descTable(Long id) {
        return null;
    }
}
