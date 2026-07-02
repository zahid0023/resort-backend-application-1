package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityGroupSearchField {
    CODE("code", FacilityGroupFilterRequest::getCode);

    private final String fieldName;
    private final Function<FacilityGroupFilterRequest, String> valueExtractor;

    FacilityGroupSearchField(String fieldName, Function<FacilityGroupFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(FacilityGroupSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
