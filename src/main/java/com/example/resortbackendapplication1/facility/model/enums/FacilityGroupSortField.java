package com.example.resortbackendapplication1.facility.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum FacilityGroupSortField {
    ID("id"),
    CODE("code"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    FacilityGroupSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityGroupSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
