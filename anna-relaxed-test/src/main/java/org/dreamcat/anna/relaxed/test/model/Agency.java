package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumnField;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "agency", index = {"id", "meal_card_type"})
public class Agency {

    @RelatedColumnField(column = "meal_card_type")
    private Integer mealCardType;
    private String manager;
    @RelatedColumn(name = "MealCard", column = "meal_card_type", relatedColumn = "type")
    private MealCard mealCard;
}
