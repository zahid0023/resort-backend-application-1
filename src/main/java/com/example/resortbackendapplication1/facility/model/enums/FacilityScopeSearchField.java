package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityScopeSearchField {
    CODE("code", FacilityScopeFilterRequest::getCode);

    private final String fieldName;
    private final Function<FacilityScopeFilterRequest, String> valueExtractor;

    FacilityScopeSearchField(String fieldName, Function<FacilityScopeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityScopeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
