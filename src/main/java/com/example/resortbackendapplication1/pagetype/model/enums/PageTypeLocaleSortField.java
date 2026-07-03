package com.example.resortbackendapplication1.pagetype.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum PageTypeLocaleSortField {
    ID("id"),
    NAME("name"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    PageTypeLocaleSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PageTypeLocaleSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
