package com.example.resortbackendapplication1.dayofweek.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum DayOfWeekSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false),
    SORT_ORDER("sortOrder", false),
    NAME("name", true),
    SHORT_NAME("shortName", true);

    private final String fieldName;
    private final boolean localeField;

    DayOfWeekSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(DayOfWeekSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(DayOfWeekSortField::isLocaleField)
                .map(DayOfWeekSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
