package org.dreamcat.anna.core.common.auth;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by tuke on 2020/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthInfo {

    private Long tenantId;
    private Long staffId;
    private Set<String> permissions;
}
