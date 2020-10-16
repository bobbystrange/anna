package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.ViewDefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by tuke on 2020/10/15
 */
public interface ViewDefDao extends JpaRepository<ViewDefEntity, Long> {

    ViewDefEntity findByTenantIdAndName(Long tenantId, String name);
}
