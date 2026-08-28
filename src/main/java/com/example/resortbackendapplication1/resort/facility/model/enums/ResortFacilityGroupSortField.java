package com.example.resortbackendapplication1.resort.facility.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortFacilityGroupSortField {

    CREATED_AT("createdAt", false),
    FACILITY_GROUP_ID("facilityGroupEntity.id", false),
    CODE("code", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    ResortFacilityGroupSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortFacilityGroupSortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortFacilityGroupSortField::isLocaleField)
                .map(ResortFacilityGroupSortField::getFieldName).collect(Collectors.toSet());
    }
}
