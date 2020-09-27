package org.dreamcat.anna.relaxed.test.model;

import lombok.Data;
import org.dreamcat.anna.relaxed.core.annotation.RelatedColumn;
import org.dreamcat.anna.relaxed.core.annotation.RelatedTable;

import java.util.List;

/**
 * Create by tuke on 2020/9/17
 */
@Data
@RelatedTable(tableName = "person", index = {"id", "code"})
public class Person {
    private String code;
    private Integer age;
    private Integer gender;
    @RelatedColumn(name = "Suits", column = "id", relatedColumn = "person_id")
    private List<Suits> suitsList;
    @RelatedColumn(name = "Student", column = "code", relatedColumn = "person_code")
    private Student student;
}
