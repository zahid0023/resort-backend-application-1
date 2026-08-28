package com.example.resortbackendapplication1.resort.core.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.core.dto.request.resort.ResortFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortSearchField implements SearchFieldSpec<ResortFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortFilterRequest::getCode);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortFilterRequest, String> valueExtractor;

    ResortSearchField(String fieldName, SearchType searchType, boolean localeField,
                      String collectionField, Function<ResortFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortSearchField::isLocaleField)
                .map(ResortSearchField::getFieldName).collect(Collectors.toSet());
    }
}
