package org.dreamcat.anna.relaxed.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by tuke on 2020/9/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameValuePair {
    private String name;
    // String, Number, or Collection
    private Object value;
}
