package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PriceTypeSearchField {
    CODE("code", PriceTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<PriceTypeFilterRequest, String> valueExtractor;

    PriceTypeSearchField(String fieldName, Function<PriceTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PriceTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
