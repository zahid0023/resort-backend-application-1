package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilitySearchField {
    CODE("code", FacilityFilterRequest::getCode);

    private final String fieldName;
    private final Function<FacilityFilterRequest, String> valueExtractor;

    FacilitySearchField(String fieldName, Function<FacilityFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilitySearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
