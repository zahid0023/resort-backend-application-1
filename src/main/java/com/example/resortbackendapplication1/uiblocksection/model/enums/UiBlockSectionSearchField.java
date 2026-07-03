package com.example.resortbackendapplication1.uiblocksection.model.enums;

import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum UiBlockSectionSearchField {
    CODE("code", UiBlockSectionFilterRequest::getCode);

    private final String fieldName;
    private final Function<UiBlockSectionFilterRequest, String> valueExtractor;

    UiBlockSectionSearchField(String fieldName, Function<UiBlockSectionFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(UiBlockSectionSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
