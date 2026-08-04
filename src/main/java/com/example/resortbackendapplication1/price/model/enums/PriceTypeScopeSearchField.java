package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PriceTypeScopeSearchField implements SearchFieldSpec<PriceTypeScopeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, PriceTypeScopeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "priceTypeScopeLocaleEntities", PriceTypeScopeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PriceTypeScopeFilterRequest, String> valueExtractor;

    PriceTypeScopeSearchField(String fieldName, SearchType searchType, boolean localeField,
                              String collectionField, Function<PriceTypeScopeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(PriceTypeScopeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(PriceTypeScopeSearchField::isLocaleField)
                .map(PriceTypeScopeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
