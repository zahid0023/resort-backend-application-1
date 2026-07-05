package com.example.resortbackendapplication1.resort.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortSortField {
    ID("id"),
    CODE("code"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
