package com.example.resortbackendapplication1.roomcategory.model.enums;

import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum RoomCategorySearchField {
    CODE("code", RoomCategoryFilterRequest::getCode);

    private final String fieldName;
    private final Function<RoomCategoryFilterRequest, String> valueExtractor;

    RoomCategorySearchField(String fieldName, Function<RoomCategoryFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(RoomCategorySearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
