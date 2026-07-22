package com.example.resortbackendapplication1.resortfacilityprice.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortFacilityPriceSortField {
    ID("id"),
    AMOUNT("amount"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortFacilityPriceSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortFacilityPriceSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
