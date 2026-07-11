package com.example.resortbackendapplication1.resortcontact.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortContactSortField {
    ID("id"),
    SORT_ORDER("sortOrder"),
    CONTACT_VALUE("contactValue"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortContactSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortContactSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
