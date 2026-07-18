package com.example.resortbackendapplication1.resortroomcategory.model.enums;

import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategorySearchField {
    CODE("code", ResortRoomCategoryFilterRequest::getCode);

    private final String fieldName;
    private final Function<ResortRoomCategoryFilterRequest, String> valueExtractor;

    ResortRoomCategorySearchField(String fieldName, Function<ResortRoomCategoryFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(ResortRoomCategorySearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
