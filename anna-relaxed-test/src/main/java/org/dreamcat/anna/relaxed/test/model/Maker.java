package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "maker")
public class Maker {
    private String name;
    private String title;
    @RelatedColumn(name = "Suit", column = "id", relatedColumn = "maker_id")
    private List<Suit> suits;
}
