package com.example.resortbackendapplication1.bedtype.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum BedTypeSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    BedTypeSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(BedTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(BedTypeSortField::isLocaleField)
                .map(BedTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
