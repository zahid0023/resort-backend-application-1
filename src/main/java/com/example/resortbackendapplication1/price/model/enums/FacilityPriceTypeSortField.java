package com.example.resortbackendapplication1.price.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum FacilityPriceTypeSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    FacilityPriceTypeSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityPriceTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(FacilityPriceTypeSortField::isLocaleField)
                .map(FacilityPriceTypeSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
