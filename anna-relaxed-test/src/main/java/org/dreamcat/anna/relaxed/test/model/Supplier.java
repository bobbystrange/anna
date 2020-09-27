package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "supplier")
public class Supplier {
    private String name;
    @RelatedColumn(name = "Room", column = "id", relatedColumn = "supplier_id")
    private List<Room> rooms;
    @RelatedColumn(name = "Chair", column = "id", relatedColumn = "supplier_id")
    private List<Desk> desks;
    @RelatedColumn(name = "Chair", column = "id", relatedColumn = "supplier_id")
    private List<Chair> chairs;
}
