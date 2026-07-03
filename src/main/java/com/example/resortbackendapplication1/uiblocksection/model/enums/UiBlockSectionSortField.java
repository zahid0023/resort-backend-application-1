package com.example.resortbackendapplication1.uiblocksection.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum UiBlockSectionSortField {
    ID("id"),
    CODE("code"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    UiBlockSectionSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(UiBlockSectionSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
