package org.dreamcat.anna.core.config;

/**
 * Create by tuke on 2021/1/30
 */
public class ValidationConfig {

    public static final String TABLE_NAME_MESSAGE = "{validation.table.table_name_is_request}";
    public static final String TABLE_NAME_PATTERN = "[a-zA-Z][0-9a-zA-Z_]{0,31}?";
    public static final String TABLE_COLUMN_NAME_MESSAGE = "{validation.table.column_name_is_request}";
    public static final String TABLE_COLUMN_NAME_PATTERN = TABLE_NAME_PATTERN;

}
