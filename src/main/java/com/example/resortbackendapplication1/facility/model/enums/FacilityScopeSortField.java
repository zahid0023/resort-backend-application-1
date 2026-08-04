package com.example.resortbackendapplication1.facility.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum FacilityScopeSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false),
    SORT_ORDER("sortOrder", false);

    private final String fieldName;
    private final boolean localeField;

    FacilityScopeSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityScopeSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(FacilityScopeSortField::isLocaleField)
                .map(FacilityScopeSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
