package org.dreamcat.anna.core.config;

import java.util.Locale;

import org.dreamcat.common.web.i18n.MessageSourceComponent;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Create by tuke on 2020/10/31
 */
@Configuration
@Import({MessageSourceComponent.class})
public class I18nConfig {
    public static final String SYSTEM_PREFIX = "system";
    public static final int SYSTEM_CODE_PREFIX = 100_000;
    public static final String TABLE_PREFIX = "table";
    public static final int TABLE_CODE_PREFIX = 101_000;

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setCookieMaxAge(3600); // just survive for an hour
        clr.setCookieName("Language");
        clr.setDefaultLocale(Locale.US);
        return clr;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // switch to a new locale based on the value of the locale parameter appended to a request
                registry.addInterceptor(new LocaleChangeInterceptor()).addPathPatterns("/**");
            }
        };
    }

    // i18n for javax.validation, it will use Accept-Language
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
