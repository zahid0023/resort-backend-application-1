package com.example.resortbackendapplication1.resortcontact.model.enums;

import com.example.resortbackendapplication1.resortcontact.dto.request.ResortContactFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortContactSearchField {
    CONTACT_VALUE("contactValue", ResortContactFilterRequest::getContactValue);

    private final String fieldName;
    private final Function<ResortContactFilterRequest, String> valueExtractor;

    ResortContactSearchField(String fieldName, Function<ResortContactFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortContactSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
