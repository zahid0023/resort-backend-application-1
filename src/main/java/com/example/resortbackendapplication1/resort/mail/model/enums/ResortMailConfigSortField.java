package com.example.resortbackendapplication1.resort.mail.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortMailConfigSortField {
    NAME("name", false),
    MAIL_PROVIDER_ID("mailProviderEntity.id", false);

    private final String fieldName;
    private final boolean localeField;

    ResortMailConfigSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortMailConfigSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(ResortMailConfigSortField::isLocaleField)
                .map(ResortMailConfigSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
