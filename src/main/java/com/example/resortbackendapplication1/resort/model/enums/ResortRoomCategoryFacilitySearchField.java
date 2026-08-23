package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategoryFacilitySearchField implements SearchFieldSpec<ResortRoomCategoryFacilityFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortRoomCategoryFacilityFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortRoomCategoryFacilityLocaleEntities", ResortRoomCategoryFacilityFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomCategoryFacilityFilterRequest, String> valueExtractor;

    ResortRoomCategoryFacilitySearchField(String fieldName, SearchType searchType, boolean localeField,
                                          String collectionField, Function<ResortRoomCategoryFacilityFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomCategoryFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomCategoryFacilitySearchField::isLocaleField)
                .map(ResortRoomCategoryFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }
}
