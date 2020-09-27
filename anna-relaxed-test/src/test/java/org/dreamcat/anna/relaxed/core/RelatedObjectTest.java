package org.dreamcat.anna.relaxed.core;

import org.dreamcat.anna.relaxed.test.model.Student;
import org.dreamcat.common.web.jackson.JacksonUtil;
import org.junit.jupiter.api.Test;

/**
 * Create by tuke on 2020/9/27
 */
public class RelatedObjectTest {

    @Test
    public void test() {
        var student = RelatedObject.parse(Student.class);
        System.out.println(JacksonUtil.toJson(student));
    }
}
