package com.roze.smarthr.config;

import com.roze.smarthr.service.LocalizationService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class LocalizationConfig {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource(LocalizationService localizationService) {
        // Create the parent message source with caching
        ReloadableResourceBundleMessageSource parentMessageSource =
                new ReloadableResourceBundleMessageSource();
        parentMessageSource.setBasename("classpath:messages");
        parentMessageSource.setDefaultEncoding("UTF-8");
        parentMessageSource.setCacheSeconds(3600); // Set cache here

        // Create composite message source
        CompositeMessageSource messageSource = new CompositeMessageSource();
        messageSource.setLocalizationService(localizationService);
        messageSource.setParentMessageSource(parentMessageSource);

        return messageSource;
    }
}