package com.example.resortbackendapplication1.contact.model.enums;

import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum CommunicationChannelSearchField {
    CODE("code", CommunicationChannelFilterRequest::getCode);

    private final String fieldName;
    private final Function<CommunicationChannelFilterRequest, String> valueExtractor;

    CommunicationChannelSearchField(String fieldName,
                                    Function<CommunicationChannelFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(CommunicationChannelSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
