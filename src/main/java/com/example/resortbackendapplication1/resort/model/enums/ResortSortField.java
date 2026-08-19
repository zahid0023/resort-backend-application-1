package com.example.resortbackendapplication1.resort.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false);

    private final String fieldName;
    private final boolean localeField;

    ResortSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(ResortSortField::isLocaleField)
                .map(ResortSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
