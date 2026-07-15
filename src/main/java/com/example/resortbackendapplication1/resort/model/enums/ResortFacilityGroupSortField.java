package com.example.resortbackendapplication1.resort.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortFacilityGroupSortField {
    ID("id"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortFacilityGroupSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortFacilityGroupSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
