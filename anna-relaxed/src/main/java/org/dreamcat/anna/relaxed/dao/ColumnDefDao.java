package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.ColumnDefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
public interface ColumnDefDao extends JpaRepository<ColumnDefEntity, Long> {

    List<ColumnDefEntity> findAllByTenantIdAndTableId(Long tenantId, Long tableId);
}
