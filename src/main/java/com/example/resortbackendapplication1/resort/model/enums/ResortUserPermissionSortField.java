package com.example.resortbackendapplication1.resort.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortUserPermissionSortField {
    ID("id"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortUserPermissionSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortUserPermissionSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
