package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "canteen")
public class Canteen {
    private String location;
    private Integer level;
    @RelatedColumn(name = "MealCard", column = "id", relatedColumn = "canteen_id")
    private List<MealCard> mealCards;
}
