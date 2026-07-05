package com.example.resortbackendapplication1.resortaccesstype.model.enums;

import com.example.resortbackendapplication1.resortaccesstype.dto.request
        .ResortAccessTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortAccessTypeSearchField {
    CODE("code", ResortAccessTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<ResortAccessTypeFilterRequest, String> valueExtractor;

    ResortAccessTypeSearchField(String fieldName, Function<ResortAccessTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortAccessTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
