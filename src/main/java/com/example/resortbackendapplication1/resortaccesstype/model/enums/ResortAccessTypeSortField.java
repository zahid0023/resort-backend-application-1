package com.example.resortbackendapplication1.resortaccesstype.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortAccessTypeSortField {
    ID("id"),
    CODE("code"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortAccessTypeSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortAccessTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
