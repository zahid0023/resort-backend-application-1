package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PriceScopeSearchField implements SearchFieldSpec<PriceScopeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, PriceScopeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "priceScopeLocaleEntities", PriceScopeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PriceScopeFilterRequest, String> valueExtractor;

    PriceScopeSearchField(String fieldName, SearchType searchType, boolean localeField,
                          String collectionField, Function<PriceScopeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(PriceScopeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(PriceScopeSearchField::isLocaleField)
                .map(PriceScopeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
