package com.example.resortbackendapplication1.mail.provider.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum MailProviderConfigSearchField {
    NAME("name", SearchType.LIKE, MailProviderConfigFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final Function<MailProviderConfigFilterRequest, String> valueExtractor;

    MailProviderConfigSearchField(String fieldName, SearchType searchType,
                                  Function<MailProviderConfigFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(MailProviderConfigSearchField::getFieldName).collect(Collectors.toSet());
    }
}
