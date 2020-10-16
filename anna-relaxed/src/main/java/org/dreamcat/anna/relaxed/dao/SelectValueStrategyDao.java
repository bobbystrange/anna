package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.SelectValueStrategyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by tuke on 2020/9/14
 */
public interface SelectValueStrategyDao extends JpaRepository<SelectValueStrategyEntity, Long> {

    List<SelectValueStrategyEntity> findAllByTenantIdAndColumnId(Long tenantId, Long columnId);
}
