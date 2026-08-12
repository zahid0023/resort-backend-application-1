package com.example.resortbackendapplication1.resortpermissiontype.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortPermissionTypeSearchField implements SearchFieldSpec<ResortPermissionTypeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortPermissionTypeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortPermissionTypeLocaleEntities", ResortPermissionTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortPermissionTypeFilterRequest, String> valueExtractor;

    ResortPermissionTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                                    String collectionField, Function<ResortPermissionTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortPermissionTypeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortPermissionTypeSearchField::isLocaleField)
                .map(ResortPermissionTypeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
