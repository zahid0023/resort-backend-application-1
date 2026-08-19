package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.ResortContactFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortContactSearchField implements SearchFieldSpec<ResortContactFilterRequest> {
    CONTACT_VALUE("contactValue", SearchType.LIKE, false, null, ResortContactFilterRequest::getContactValue);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortContactFilterRequest, String> valueExtractor;

    ResortContactSearchField(String fieldName, SearchType searchType, boolean localeField,
                             String collectionField, Function<ResortContactFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortContactSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortContactSearchField::isLocaleField)
                .map(ResortContactSearchField::getFieldName).collect(Collectors.toSet());
    }
}
