package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "suit_maintenance_relation", index = {"id", "suit_id", "maintenance_seq"})
public class SuitMaintenanceRelation {

    @RelatedColumn(name = "Suit", column = "suit_id", relatedColumn = "id")
    private List<Suit> suits;
    @RelatedColumn(name = "Maintenance", column = "maintenance_seq", relatedColumn = "seq")
    private List<Maintenance> maintenances;
}
