package org.dreamcat.anna.relaxed.test.model;

import java.util.List;
import lombok.Data;
import org.dreamcat.anna.relaxed.common.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.common.annotation.RelatedTable;

/**
 * Create by tuke on 2020/9/18
 */
@Data
@RelatedTable(tableName = "chair_maintenance_relation", index = {"id", "chair_seq",
        "maintenance_id"})
public class ChairMaintenanceRelation {

    @RelatedColumn(name = "Chair", column = "chair_seq", relatedColumn = "seq")
    private List<Chair> chairs;
    @RelatedColumn(name = "Maintenance", column = "maintenance_id", relatedColumn = "id")
    private List<Maintenance> maintenances;
}
