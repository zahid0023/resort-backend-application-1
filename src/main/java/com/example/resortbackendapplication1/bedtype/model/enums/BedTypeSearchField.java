package com.example.resortbackendapplication1.bedtype.model.enums;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum BedTypeSearchField {
    CODE("code", BedTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<BedTypeFilterRequest, String> valueExtractor;

    BedTypeSearchField(String fieldName, Function<BedTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(BedTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
