package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "door", index = {"id", "code", "supplier_id"})
public class Door {

    private String code;
    private String size;
    @RelatedColumn(name = "Supplier", column = "supplier_id", relatedColumn = "id")
    private Supplier supplier;
}
