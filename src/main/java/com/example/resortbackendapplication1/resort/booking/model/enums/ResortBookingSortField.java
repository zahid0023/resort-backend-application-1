package com.example.resortbackendapplication1.resort.booking.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortBookingSortField {

    CREATED_AT("createdAt", false);

    private final String fieldName;
    private final boolean localeField;

    ResortBookingSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortBookingSortField::getFieldName).collect(Collectors.toSet());
    }
}
