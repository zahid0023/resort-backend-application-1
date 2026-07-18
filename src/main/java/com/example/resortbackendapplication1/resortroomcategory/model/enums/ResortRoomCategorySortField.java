package com.example.resortbackendapplication1.resortroomcategory.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortRoomCategorySortField {
    ID("id"),
    CODE("code"),
    SORT_ORDER("sortOrder"),
    MAX_ADULTS("maxAdults"),
    MAX_OCCUPANCY("maxOccupancy"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortRoomCategorySortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortRoomCategorySortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
