package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.RowDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
public interface RowDataDao extends JpaRepository<RowDataEntity, Long> {

    List<RowDataEntity> findAllByTenantIdAndColumnIdAndPrimaryValue(Long tenantId, Long columnId, String primaryValue);

    List<RowDataEntity> findAllByTenantIdAndPrimaryValue(Long tenantId, String primaryValue);
}
