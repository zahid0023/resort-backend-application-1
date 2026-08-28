package com.example.resortbackendapplication1.roomstatus.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum RoomStatusSortField {
    CREATED_AT("createdAt", false),
    CODE("code", false);

    private final String fieldName;
    private final boolean localeField;

    RoomStatusSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(RoomStatusSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(RoomStatusSortField::isLocaleField)
                .map(RoomStatusSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
