package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.ResortRoomCategoryFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategorySearchField implements SearchFieldSpec<ResortRoomCategoryFilterRequest> {
    ;

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomCategoryFilterRequest, String> valueExtractor;

    ResortRoomCategorySearchField(String fieldName, SearchType searchType, boolean localeField,
                                  String collectionField, Function<ResortRoomCategoryFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomCategorySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomCategorySearchField::isLocaleField)
                .map(ResortRoomCategorySearchField::getFieldName).collect(Collectors.toSet());
    }
}
