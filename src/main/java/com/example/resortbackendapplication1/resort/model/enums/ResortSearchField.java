package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortSearchField {
    CODE("code", ResortFilterRequest::getCode);

    private final String fieldName;
    private final Function<ResortFilterRequest, String> valueExtractor;

    ResortSearchField(String fieldName, Function<ResortFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
