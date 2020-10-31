package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "suit", index = {"id", "maker_id", "suits_id", "person_id"})
public class Suit {

    private Long price;
    private String color;
    @RelatedColumn(name = "Maker", column = "maker_id", relatedColumn = "id")
    private Maker maker;
    @RelatedColumn(name = "SuitMaintenanceRelation", column = "id", relatedColumn = "suit_id")
    private List<SuitMaintenanceRelation> suitMaintenanceRelations;
    @RelatedColumn(name = "Suits", column = "suits_id", relatedColumn = "id")
    private Suits suits;
    @RelatedColumn(name = "Person", column = "person_id", relatedColumn = "id")
    private Person person;
}
