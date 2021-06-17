package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/17
 */
@Data
@RelatedTable(tableName = "meal_card", index = {"id", "canteen_id", "student_id"})
public class MealCard {

    private String number;
    private Integer type;
    @RelatedColumn(name = "Canteen", column = "canteen_id", relatedColumn = "id")
    private Canteen canteen;
    @RelatedColumn(name = "Agency", column = "id", relatedColumn = "meal_card_type")
    private List<Agency> agencies;
    @RelatedColumn(name = "Student", column = "student_id", relatedColumn = "id")
    private Student student;
}
