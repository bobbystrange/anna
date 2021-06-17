package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "suits", index = {"id", "person_id"})
public class Suits {

    private String number;
    @RelatedColumn(name = "Suit", column = "id", relatedColumn = "suits_id")
    private List<Suit> suitList;
    @RelatedColumn(name = "Person", column = "person_id", relatedColumn = "id")
    private Person person;
}
