package org.dreamcat.anna.core.dao;

import static org.dreamcat.anna.core.config.i18n.TableMessageCode.TABLE_ID_OR_NAME_NOT_FOUND;

import java.util.Objects;
import org.dreamcat.anna.core.entity.TableDefEntity;
import org.dreamcat.common.web.exception.NotFoundMessageException;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by tuke on 2020/9/14
 */
public interface TableDefDao extends JpaRepository<TableDefEntity, Long> {

    TableDefEntity findByNameAndTenantId(String name, Long tenantId);

    default TableDefEntity fetchTable(String name, Long tenantId) {
        var tableDefEntity = findByNameAndTenantId(name, tenantId);
        if (tableDefEntity == null) {
            throw new NotFoundMessageException(TABLE_ID_OR_NAME_NOT_FOUND, name);
        }
        return tableDefEntity;
    }

    default TableDefEntity fetchTable(Long id, Long tenantId) {
        var tableDefEntity = findById(id).orElse(null);
        if (tableDefEntity == null || !Objects.equals(tableDefEntity.getTenantId(), tenantId)) {
            throw new NotFoundMessageException(TABLE_ID_OR_NAME_NOT_FOUND, id);
        }
        return tableDefEntity;
    }
}

