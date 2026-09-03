package com.example.resortbackendapplication1.mail.provider.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum MailProviderSearchField {
    CODE("code", SearchType.LIKE, MailProviderFilterRequest::getCode),
    NAME("name", SearchType.LIKE, MailProviderFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final Function<MailProviderFilterRequest, String> valueExtractor;

    MailProviderSearchField(String fieldName, SearchType searchType,
                             Function<MailProviderFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(MailProviderSearchField::getFieldName).collect(Collectors.toSet());
    }
}
