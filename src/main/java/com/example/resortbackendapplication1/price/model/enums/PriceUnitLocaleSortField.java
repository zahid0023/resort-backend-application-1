package com.example.resortbackendapplication1.price.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum PriceUnitLocaleSortField {
    ID("id"),
    NAME("name"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    PriceUnitLocaleSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PriceUnitLocaleSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
