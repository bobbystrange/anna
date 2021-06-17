package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumnField;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/17
 */
@Data
@RelatedTable(tableName = "student", index = {"id", "number", "person_code", "room_id"})
public class Student {

    private String number;
    private String name;
    @RelatedColumnField(column = "person_code")
    private String personCode;
    @RelatedColumnField(column = "room_id")
    private Long roomId;
    @RelatedColumn(name = "MealCard", column = "id", relatedColumn = "student_id")
    private List<MealCard> mealCards;
    @RelatedColumn(name = "Person", column = "person_code", relatedColumn = "code")
    private Person person;
    @RelatedColumn(name = "Room", column = "room_id", relatedColumn = "id")
    private Room room;
}
