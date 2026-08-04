package com.example.resortbackendapplication1.contact.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.ContactTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ContactTypeSearchField implements SearchFieldSpec<ContactTypeFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ContactTypeFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "contactTypeLocaleEntities", ContactTypeFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ContactTypeFilterRequest, String> valueExtractor;

    ContactTypeSearchField(String fieldName, SearchType searchType, boolean localeField,
                           String collectionField, Function<ContactTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ContactTypeSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ContactTypeSearchField::isLocaleField)
                .map(ContactTypeSearchField::getFieldName).collect(Collectors.toSet());
    }
}
