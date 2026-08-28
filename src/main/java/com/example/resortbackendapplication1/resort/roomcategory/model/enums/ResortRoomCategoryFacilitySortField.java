package com.example.resortbackendapplication1.resort.roomcategory.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategoryFacilitySortField {

    CREATED_AT("createdAt", false),
    RESORT_ROOM_CATEGORY_FACILITY_GROUP_ID("resortRoomCategoryFacilityGroupEntity.id", false),
    FACILITY_ID("facilityEntity.id", false),
    CODE("code", false),
    IS_HIGHLIGHTED("isHighlighted", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    ResortRoomCategoryFacilitySortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomCategoryFacilitySortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortRoomCategoryFacilitySortField::isLocaleField)
                .map(ResortRoomCategoryFacilitySortField::getFieldName).collect(Collectors.toSet());
    }
}
