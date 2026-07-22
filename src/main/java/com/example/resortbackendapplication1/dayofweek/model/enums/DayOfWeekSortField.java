package com.example.resortbackendapplication1.dayofweek.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DayOfWeekSortField {
    ID("id"),
    CODE("code"),
    ISO_DAY_NUMBER("isoDayNumber"),
    DISPLAY_ORDER("displayOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    DayOfWeekSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(DayOfWeekSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
