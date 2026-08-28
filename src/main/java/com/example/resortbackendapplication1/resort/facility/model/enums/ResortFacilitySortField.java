package com.example.resortbackendapplication1.resort.facility.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortFacilitySortField {

    CREATED_AT("createdAt", false),
    RESORT_FACILITY_GROUP_ID("resortFacilityGroupEntity.id", false),
    FACILITY_ID("facilityEntity.id", false),
    CODE("code", false),
    IS_HIGHLIGHTED("isHighlighted", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    ResortFacilitySortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortFacilitySortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortFacilitySortField::isLocaleField)
                .map(ResortFacilitySortField::getFieldName).collect(Collectors.toSet());
    }
}
