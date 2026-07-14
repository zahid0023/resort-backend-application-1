package com.example.resortbackendapplication1.resort.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortUserSortField {
    ID("id"),
    JOINED_AT("joinedAt"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortUserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortUserSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
