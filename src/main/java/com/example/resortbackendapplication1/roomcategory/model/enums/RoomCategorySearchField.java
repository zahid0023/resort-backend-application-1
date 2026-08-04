package com.example.resortbackendapplication1.roomcategory.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum RoomCategorySearchField implements SearchFieldSpec<RoomCategoryFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, RoomCategoryFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "roomCategoryLocaleEntities", RoomCategoryFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<RoomCategoryFilterRequest, String> valueExtractor;

    RoomCategorySearchField(String fieldName, SearchType searchType, boolean localeField,
                            String collectionField, Function<RoomCategoryFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(RoomCategorySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(RoomCategorySearchField::isLocaleField)
                .map(RoomCategorySearchField::getFieldName).collect(Collectors.toSet());
    }
}
