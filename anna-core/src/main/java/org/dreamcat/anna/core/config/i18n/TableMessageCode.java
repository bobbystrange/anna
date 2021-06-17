package org.dreamcat.anna.core.config.i18n;

import static org.dreamcat.anna.core.config.I18nConfig.TABLE_CODE_PREFIX;
import static org.dreamcat.anna.core.config.I18nConfig.TABLE_PREFIX;

import lombok.Getter;
import org.dreamcat.common.web.core.MessageCode;

/**
 * Create by tuke on 2020/11/2
 */
@Getter
public enum TableMessageCode implements MessageCode {
    TABLE_COLUMNS_HAVE_DUPLICATE_NAMES(1),
    TABLE_ALREADY_EXISTS(2),
    TABLE_ID_OR_NAME_NOT_FOUND(3),
    ;

    private final int code;
    private final String value;

    TableMessageCode(int code) {
        this.code = TABLE_CODE_PREFIX + code;
        this.value = TABLE_PREFIX + "." + name().toLowerCase();
    }
}
