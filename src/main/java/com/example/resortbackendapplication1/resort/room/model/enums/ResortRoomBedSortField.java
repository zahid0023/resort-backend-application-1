package com.example.resortbackendapplication1.resort.room.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomBedSortField {

    CREATED_AT("createdAt", false);

    private final String fieldName;
    private final boolean localeField;

    ResortRoomBedSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomBedSortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortRoomBedSortField::isLocaleField)
                .map(ResortRoomBedSortField::getFieldName).collect(Collectors.toSet());
    }
}
