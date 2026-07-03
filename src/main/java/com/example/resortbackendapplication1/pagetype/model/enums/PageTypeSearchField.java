package com.example.resortbackendapplication1.pagetype.model.enums;

import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PageTypeSearchField {
    CODE("code", PageTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<PageTypeFilterRequest, String> valueExtractor;

    PageTypeSearchField(String fieldName, Function<PageTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PageTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
