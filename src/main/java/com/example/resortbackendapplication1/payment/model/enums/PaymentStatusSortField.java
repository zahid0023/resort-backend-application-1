package com.example.resortbackendapplication1.payment.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum PaymentStatusSortField {
    CREATED_AT("createdAt", false);

    private final String fieldName;
    private final boolean localeField;

    PaymentStatusSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PaymentStatusSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(PaymentStatusSortField::isLocaleField)
                .map(PaymentStatusSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
