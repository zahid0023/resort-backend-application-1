package com.example.resortbackendapplication1.mail.provider.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum MailProviderSortField {
    CODE("code"),
    NAME("name");

    private final String fieldName;

    MailProviderSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(MailProviderSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
