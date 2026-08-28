package com.example.resortbackendapplication1.resort.room.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomFacilitySearchField implements SearchFieldSpec<ResortRoomFacilityFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortRoomFacilityFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortRoomFacilityLocaleEntities", ResortRoomFacilityFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomFacilityFilterRequest, String> valueExtractor;

    ResortRoomFacilitySearchField(String fieldName, SearchType searchType, boolean localeField,
                                  String collectionField, Function<ResortRoomFacilityFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomFacilitySearchField::isLocaleField)
                .map(ResortRoomFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }
}
