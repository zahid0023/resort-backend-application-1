package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.FacilityScopeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityScopeSearchField implements SearchFieldSpec<FacilityScopeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, FacilityScopeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "facilityScopeLocaleEntities", FacilityScopeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<FacilityScopeFilterRequest, String> valueExtractor;

    FacilityScopeSearchField(String fieldName, SearchType searchType, boolean localeField,
                             String collectionField, Function<FacilityScopeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(FacilityScopeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(FacilityScopeSearchField::isLocaleField)
                .map(FacilityScopeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
