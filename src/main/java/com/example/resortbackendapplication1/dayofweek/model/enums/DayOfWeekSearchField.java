package com.example.resortbackendapplication1.dayofweek.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum DayOfWeekSearchField implements SearchFieldSpec<DayOfWeekFilterRequest> {
    // direct entity String fields:
    CODE("code", SearchType.LIKE, false, null, DayOfWeekFilterRequest::getCode),
    // locale child String fields (require JOIN):
    NAME("name", SearchType.LIKE, true, "dayOfWeekLocaleEntities", DayOfWeekFilterRequest::getName),
    SHORT_NAME("shortName", SearchType.LIKE, true, "dayOfWeekLocaleEntities", DayOfWeekFilterRequest::getShortName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<DayOfWeekFilterRequest, String> valueExtractor;

    DayOfWeekSearchField(String fieldName, SearchType searchType, boolean localeField,
                         String collectionField,
                         Function<DayOfWeekFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(DayOfWeekSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(DayOfWeekSearchField::isLocaleField)
                .map(DayOfWeekSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
