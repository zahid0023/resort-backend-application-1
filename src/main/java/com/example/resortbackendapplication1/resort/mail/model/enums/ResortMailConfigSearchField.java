package com.example.resortbackendapplication1.resort.mail.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortMailConfigSearchField implements SearchFieldSpec<ResortMailConfigFilterRequest> {
    NAME("name", SearchType.LIKE, false, null, ResortMailConfigFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortMailConfigFilterRequest, String> valueExtractor;

    ResortMailConfigSearchField(String fieldName, SearchType searchType, boolean localeField,
                                String collectionField, Function<ResortMailConfigFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortMailConfigSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortMailConfigSearchField::isLocaleField)
                .map(ResortMailConfigSearchField::getFieldName).collect(Collectors.toSet());
    }
}
