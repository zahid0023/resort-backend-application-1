package com.example.resortbackendapplication1.resortroletype.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoleTypeSearchField implements SearchFieldSpec<ResortRoleTypeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortRoleTypeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortRoleTypeLocaleEntities", ResortRoleTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoleTypeFilterRequest, String> valueExtractor;

    ResortRoleTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                              String collectionField, Function<ResortRoleTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoleTypeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoleTypeSearchField::isLocaleField)
                .map(ResortRoleTypeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
