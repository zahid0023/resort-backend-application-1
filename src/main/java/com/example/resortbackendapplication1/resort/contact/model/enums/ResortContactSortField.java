package com.example.resortbackendapplication1.resort.contact.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortContactSortField {
    CREATED_AT("createdAt", false),
    CONTACT_TYPE_ID("contactTypeEntity.id", false),
    COMMUNICATION_CHANNEL_ID("communicationChannelEntity.id", false),
    CONTACT_VALUE("contactValue", false),
    IS_PRIMARY("isPrimary", false);

    private final String fieldName;
    private final boolean localeField;

    ResortContactSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortContactSortField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values())
                .filter(ResortContactSortField::isLocaleField)
                .map(ResortContactSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
