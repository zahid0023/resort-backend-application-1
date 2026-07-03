package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PriceUnitSearchField {
    CODE("code", PriceUnitFilterRequest::getCode);

    private final String fieldName;
    private final Function<PriceUnitFilterRequest, String> valueExtractor;

    PriceUnitSearchField(String fieldName, Function<PriceUnitFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PriceUnitSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
