package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "maintenance", index = {"id", "seq"})
public class Maintenance {
    private String seq;
    private String standard;

    @RelatedColumn(name = "SuitMaintenanceRelation", column = "seq", relatedColumn = "maintenance_seq")
    private List<SuitMaintenanceRelation> suitMaintenanceRelations;
    @RelatedColumn(name = "ChairMaintenanceRelation", column = "id", relatedColumn = "maintenance_id")
    private List<ChairMaintenanceRelation> chairMaintenanceRelations;
}
