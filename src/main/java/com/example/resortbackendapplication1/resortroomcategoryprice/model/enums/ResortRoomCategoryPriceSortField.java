package com.example.resortbackendapplication1.resortroomcategoryprice.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ResortRoomCategoryPriceSortField {
    ID("id"),
    AMOUNT("amount"),
    PRIORITY("priority"),
    VALID_FROM("validFrom"),
    VALID_TO("validTo"),
    CREATED_AT("createdAt");

    private final String fieldName;

    ResortRoomCategoryPriceSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortRoomCategoryPriceSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
