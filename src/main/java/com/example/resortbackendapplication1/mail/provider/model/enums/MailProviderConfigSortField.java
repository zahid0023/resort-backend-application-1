package com.example.resortbackendapplication1.mail.provider.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum MailProviderConfigSortField {
    NAME("name"),
    MAIL_PROVIDER_ID("mailProviderEntity.id");

    private final String fieldName;

    MailProviderConfigSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(MailProviderConfigSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
