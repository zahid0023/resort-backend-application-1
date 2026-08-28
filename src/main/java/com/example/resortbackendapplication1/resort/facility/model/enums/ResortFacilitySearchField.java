package com.example.resortbackendapplication1.resort.facility.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.ResortFacilityFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortFacilitySearchField implements SearchFieldSpec<ResortFacilityFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortFacilityFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortFacilityLocaleEntities", ResortFacilityFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortFacilityFilterRequest, String> valueExtractor;

    ResortFacilitySearchField(String fieldName, SearchType searchType, boolean localeField,
                              String collectionField, Function<ResortFacilityFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortFacilitySearchField::isLocaleField)
                .map(ResortFacilitySearchField::getFieldName).collect(Collectors.toSet());
    }
}
