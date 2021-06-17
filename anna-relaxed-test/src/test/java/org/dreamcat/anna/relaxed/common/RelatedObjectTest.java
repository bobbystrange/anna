package org.dreamcat.anna.relaxed.common;

import org.dreamcat.anna.relaxed.test.model.Student;
import org.dreamcat.common.x.jackson.JacksonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Create by tuke on 2020/9/27
 */
class RelatedObjectTest {

    @Test
    void test() {
        var student = RelatedObject.parse(Student.class);
        var json = JacksonUtil.toJson(student);
        assertNotNull(json);
        System.out.println(json);
    }
}
