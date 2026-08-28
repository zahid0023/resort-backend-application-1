package com.example.resortbackendapplication1.resort.room.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomFacilitySortField {

    CREATED_AT("createdAt", false),
    RESORT_ROOM_FACILITY_GROUP_ID("resortRoomFacilityGroupEntity.id", false),
    FACILITY_ID("facilityEntity.id", false),
    CODE("code", false),
    IS_HIGHLIGHTED("isHighlighted", false),
    NAME("name", true);

    private final String fieldName;
    private final boolean localeField;

    ResortRoomFacilitySortField(String fieldName, boolean localeField) {
        this.fieldName = fieldName;
        this.localeField = localeField;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomFacilitySortField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSortFields() {
        return Arrays.stream(values()).filter(ResortRoomFacilitySortField::isLocaleField)
                .map(ResortRoomFacilitySortField::getFieldName).collect(Collectors.toSet());
    }
}
