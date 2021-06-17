package org.dreamcat.anna.core;

import org.dreamcat.common.web.handler.RestResponseBodyAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Create by tuke on 2020/10/31
 */
@Import({RestResponseBodyAdvice.class})
@SpringBootApplication
public class AnnaCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnaCoreApplication.class, args);
    }
}
