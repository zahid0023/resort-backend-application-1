package com.example.resortbackendapplication1.facility.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum FacilitySortField {

    CREATED_AT("createdAt", false),
    SORT_ORDER("sortOrder", false),
    CODE("code", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    FacilitySortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(FacilitySortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(FacilitySortField::isLocaleField)
                .map(FacilitySortField::getFieldName).collect(Collectors.toSet());
    }
}
