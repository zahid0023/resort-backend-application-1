package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilitySearchField implements SearchFieldSpec<FacilityFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, FacilityFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "facilityLocaleEntities", FacilityFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<FacilityFilterRequest, String> valueExtractor;

    FacilitySearchField(String fieldName, SearchType searchType, boolean localeField,
                        String collectionField, Function<FacilityFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(FacilitySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(FacilitySearchField::isLocaleField)
                .map(FacilitySearchField::getFieldName).collect(Collectors.toSet());
    }
}
