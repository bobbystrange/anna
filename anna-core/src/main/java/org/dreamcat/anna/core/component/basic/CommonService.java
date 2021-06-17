package org.dreamcat.anna.core.component.basic;

import java.util.function.Supplier;
import javax.annotation.Resource;
import org.dreamcat.anna.core.common.auth.AuthInfo;
import org.dreamcat.anna.core.config.AuthConfig;
import org.dreamcat.anna.core.entity.BaseEntity;
import org.dreamcat.common.web.security.jwt.JwtServletFactory;
import org.springframework.stereotype.Component;

/**
 * Create by tuke on 2021/1/29
 */
@Component
public class CommonService {

    @Resource
    private JwtServletFactory jwtServletFactory;

    public AuthInfo getAuthInfo() {
        var jwt = jwtServletFactory.getJwt();
        return AuthInfo.builder()
                .tenantId((Long) jwt.getClaims().get(AuthConfig.TENANT_ID))
                .staffId((Long) jwt.getClaims().get(AuthConfig.STAFF_ID))
                .permissions(jwt.getPermissions())
                .build();
    }

    public <T extends BaseEntity> T newEntity(AuthInfo authInfo, Supplier<T> constructor) {
        var tenantId = authInfo.getTenantId();
        var staffId = authInfo.getStaffId();

        var target = constructor.get();
        target.setCreatedBy(staffId);
        target.setUpdatedBy(staffId);
        target.setTenantId(tenantId);
        return target;
    }
}
