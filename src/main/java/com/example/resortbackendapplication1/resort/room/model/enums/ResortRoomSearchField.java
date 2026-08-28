package com.example.resortbackendapplication1.resort.room.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomSearchField implements SearchFieldSpec<ResortRoomFilterRequest> {
    ;

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomFilterRequest, String> valueExtractor;

    ResortRoomSearchField(String fieldName, SearchType searchType, boolean localeField,
                          String collectionField, Function<ResortRoomFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomSearchField::isLocaleField)
                .map(ResortRoomSearchField::getFieldName).collect(Collectors.toSet());
    }
}
