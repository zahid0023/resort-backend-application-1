package com.example.resortbackendapplication1.roomstatus.model.enums;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusFilterRequest;
import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum RoomStatusSearchField implements SearchFieldSpec<RoomStatusFilterRequest> {
    // direct entity String fields:
    CODE("code", SearchType.LIKE, false, null, RoomStatusFilterRequest::getCode);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<RoomStatusFilterRequest, String> valueExtractor;

    RoomStatusSearchField(String fieldName, SearchType searchType, boolean localeField,
                                String collectionField,
                                Function<RoomStatusFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(RoomStatusSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(RoomStatusSearchField::isLocaleField)
                .map(RoomStatusSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
