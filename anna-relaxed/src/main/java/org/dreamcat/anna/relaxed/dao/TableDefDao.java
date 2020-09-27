package org.dreamcat.anna.relaxed.dao;

import org.dreamcat.anna.relaxed.entity.TableDefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by tuke on 2020/9/14
 */
public interface TableDefDao extends JpaRepository<TableDefEntity, Long> {

    TableDefEntity findByTenantIdAndName(Long tenantId, String name);
}
