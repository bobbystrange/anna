package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/17
 */
@Data
@RelatedTable(tableName = "room", index = {"id", "name", "door_code"})
public class Room {

    private String name;
    private Long area;
    @RelatedColumn(name = "Desk", column = "name", relatedColumn = "room_name")
    private List<Desk> desks;
    @RelatedColumn(name = "Door", column = "door_code", relatedColumn = "code")
    private Door door;
    @RelatedColumn(name = "Student", column = "id", relatedColumn = "room_id")
    private List<Student> students;
}
