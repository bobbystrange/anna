package org.dreamcat.anna.core.common.query;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Create by tuke on 2021/1/30
 */
@Getter
@Setter
@ToString
public class TableIdQuery {
    @NotNull
    private Long tableId;
}
