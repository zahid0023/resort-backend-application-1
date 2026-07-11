package com.example.resortbackendapplication1.contact.model.enums;

import com.example.resortbackendapplication1.contact.dto.request.ContactTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ContactTypeSearchField {
    CODE("code", ContactTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<ContactTypeFilterRequest, String> valueExtractor;

    ContactTypeSearchField(String fieldName, Function<ContactTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ContactTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
