package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumnField;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "desk", index = {"id", "room_name", "supplier_id"})
public class Desk {

    @RelatedColumnField(column = "room_name")
    private String roomName;
    private Integer height;
    @RelatedColumn(name = "Chair", column = "id", relatedColumn = "desk_id")
    private List<Chair> chairs;
    @RelatedColumn(name = "Supplier", column = "supplier_id", relatedColumn = "id")
    private Supplier supplier;
    @RelatedColumn(name = "Room", column = "room_name", relatedColumn = "name")
    private Room room;
}
