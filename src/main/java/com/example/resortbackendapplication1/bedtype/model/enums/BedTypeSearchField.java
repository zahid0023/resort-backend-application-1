package com.example.resortbackendapplication1.bedtype.model.enums;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum BedTypeSearchField implements SearchFieldSpec<BedTypeFilterRequest> {
    // direct entity String fields:
    CODE("code", SearchType.LIKE, false, null, BedTypeFilterRequest::getCode),
    // locale child String fields (require JOIN):
    NAME("name", SearchType.LIKE, true, "bedTypeLocaleEntities", BedTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<BedTypeFilterRequest, String> valueExtractor;

    BedTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                       String collectionField,
                       Function<BedTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(BedTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(BedTypeSearchField::isLocaleField)
                .map(BedTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
