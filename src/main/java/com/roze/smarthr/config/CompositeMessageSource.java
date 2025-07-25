package com.roze.smarthr.config;

import com.roze.smarthr.service.LocalizationService;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class CompositeMessageSource implements MessageSource {
    private MessageSource parentMessageSource;
    private LocalizationService localizationService;

    public void setParentMessageSource(MessageSource parentMessageSource) {
        this.parentMessageSource = parentMessageSource;
    }

    public void setLocalizationService(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        // First try database
        String translation = localizationService.getTranslatedText("Message", 0L, code, locale.getLanguage());
        if (translation != null) {
            return translation;
        }
        
        // Fallback to properties file
        return parentMessageSource.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        String message = getMessage(code, args, null, locale);
        if (message == null) {
            throw new NoSuchMessageException(code, locale);
        }
        return message;
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return parentMessageSource.getMessage(resolvable, locale);
    }
}