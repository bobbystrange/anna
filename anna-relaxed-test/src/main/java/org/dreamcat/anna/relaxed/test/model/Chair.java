package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "chair", index = {"id", "seq", "desk_id", "supplier_id"})
public class Chair {
    private String seq;
    private Integer weight;
    @RelatedColumn(name = "ChairMaintenanceRelation", column = "seq", relatedColumn = "chair_seq")
    private List<ChairMaintenanceRelation> chairMaintenanceRelations;
    @RelatedColumn(name = "Supplier", column = "supplier_id", relatedColumn = "id")
    private Supplier supplier;
    @RelatedColumn(name = "Desk", column = "desk_id", relatedColumn = "id")
    private Desk desk;
}
