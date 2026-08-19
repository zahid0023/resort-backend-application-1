package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortFacilityGroupSearchField implements SearchFieldSpec<ResortFacilityGroupFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortFacilityGroupFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortFacilityGroupLocaleEntities", ResortFacilityGroupFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortFacilityGroupFilterRequest, String> valueExtractor;

    ResortFacilityGroupSearchField(String fieldName, SearchType searchType, boolean localeField,
                                   String collectionField, Function<ResortFacilityGroupFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortFacilityGroupSearchField::isLocaleField)
                .map(ResortFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }
}
