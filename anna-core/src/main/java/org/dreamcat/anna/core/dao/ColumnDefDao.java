package org.dreamcat.anna.core.dao;

import java.util.List;
import org.dreamcat.anna.core.entity.ColumnDefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by tuke on 2020/9/14
 */
public interface ColumnDefDao extends JpaRepository<ColumnDefEntity, Long> {

    List<ColumnDefEntity> findAllByTableIdAndTenantId(Long tenantId, Long tableId);
}

