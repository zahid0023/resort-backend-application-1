package com.example.resortbackendapplication1.resortpermissiontype.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortPermissionTypeSortField {
    ID("id"),
    CODE("code"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortPermissionTypeSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortPermissionTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
