package com.example.resortbackendapplication1.facilitypricetype.model.enums;

import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityPriceTypeSearchField {
    CODE("code", FacilityPriceTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<FacilityPriceTypeFilterRequest, String> valueExtractor;

    FacilityPriceTypeSearchField(String fieldName, Function<FacilityPriceTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityPriceTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
