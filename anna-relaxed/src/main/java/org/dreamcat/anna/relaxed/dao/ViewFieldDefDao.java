package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.ViewFieldDefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by tuke on 2020/10/15
 */
public interface ViewFieldDefDao extends JpaRepository<ViewFieldDefEntity, Long> {

    List<ViewFieldDefEntity> findAllByTenantIdAndViewId(Long tenantId, Long viewId);
}
