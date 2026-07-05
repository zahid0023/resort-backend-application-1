package com.example.resortbackendapplication1.resortpermissiontype.model.enums;

import com.example.resortbackendapplication1.resortpermissiontype.dto.request.ResortPermissionTypeFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortPermissionTypeSearchField {
    CODE("code", ResortPermissionTypeFilterRequest::getCode);

    private final String fieldName;
    private final Function<ResortPermissionTypeFilterRequest, String> valueExtractor;

    ResortPermissionTypeSearchField(String fieldName,
                                    Function<ResortPermissionTypeFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortPermissionTypeSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
