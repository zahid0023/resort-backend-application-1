package com.example.resortbackendapplication1.facility.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum FacilityGroupSearchField implements SearchFieldSpec<FacilityGroupFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, FacilityGroupFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "facilityGroupLocaleEntities", FacilityGroupFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<FacilityGroupFilterRequest, String> valueExtractor;

    FacilityGroupSearchField(String fieldName, SearchType searchType, boolean localeField,
                             String collectionField, Function<FacilityGroupFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(FacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(FacilityGroupSearchField::isLocaleField)
                .map(FacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }
}
