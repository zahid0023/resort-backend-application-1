package com.example.resortbackendapplication1.dayofweek.model.enums;

import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum DayOfWeekSearchField {
    CODE("code", DayOfWeekFilterRequest::getCode);

    private final String fieldName;
    private final Function<DayOfWeekFilterRequest, String> valueExtractor;

    DayOfWeekSearchField(String fieldName, Function<DayOfWeekFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(DayOfWeekSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
