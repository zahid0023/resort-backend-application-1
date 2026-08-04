package com.example.resortbackendapplication1.contact.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CommunicationChannelFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum CommunicationChannelSearchField implements SearchFieldSpec<CommunicationChannelFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, CommunicationChannelFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "communicationChannelLocaleEntities", CommunicationChannelFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<CommunicationChannelFilterRequest, String> valueExtractor;

    CommunicationChannelSearchField(String fieldName, SearchType searchType, boolean localeField,
                                    String collectionField, Function<CommunicationChannelFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(CommunicationChannelSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(CommunicationChannelSearchField::isLocaleField)
                .map(CommunicationChannelSearchField::getFieldName).collect(Collectors.toSet());
    }
}
