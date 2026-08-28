package com.example.resortbackendapplication1.resort.roomcategory.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategoryFacilityGroupSortField {

    CREATED_AT("createdAt", false),
    FACILITY_GROUP_ID("facilityGroupEntity.id", false),
    CODE("code", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    ResortRoomCategoryFacilityGroupSortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomCategoryFacilityGroupSortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortRoomCategoryFacilityGroupSortField::isLocaleField)
                .map(ResortRoomCategoryFacilityGroupSortField::getFieldName).collect(Collectors.toSet());
    }
}
