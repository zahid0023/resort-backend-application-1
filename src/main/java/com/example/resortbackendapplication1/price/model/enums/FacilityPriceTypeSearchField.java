package com.example.resortbackendapplication1.price.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityPriceTypeSearchField implements SearchFieldSpec<FacilityPriceTypeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, FacilityPriceTypeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "facilityPriceTypeLocaleEntities", FacilityPriceTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<FacilityPriceTypeFilterRequest, String> valueExtractor;

    FacilityPriceTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                         String collectionField, Function<FacilityPriceTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(FacilityPriceTypeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(FacilityPriceTypeSearchField::isLocaleField)
                .map(FacilityPriceTypeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
