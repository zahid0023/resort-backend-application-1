package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum PriceTypeSearchField implements SearchFieldSpec<PriceTypeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, PriceTypeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "priceTypeLocaleEntities", PriceTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PriceTypeFilterRequest, String> valueExtractor;

    PriceTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                         String collectionField, Function<PriceTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(PriceTypeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(PriceTypeSearchField::isLocaleField)
                .map(PriceTypeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
