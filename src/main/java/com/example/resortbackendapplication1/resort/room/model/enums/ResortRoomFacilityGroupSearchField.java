package com.example.resortbackendapplication1.resort.room.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomFacilityGroupSearchField implements SearchFieldSpec<ResortRoomFacilityGroupFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortRoomFacilityGroupFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortRoomFacilityGroupLocaleEntities", ResortRoomFacilityGroupFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomFacilityGroupFilterRequest, String> valueExtractor;

    ResortRoomFacilityGroupSearchField(String fieldName, SearchType searchType, boolean localeField,
                                       String collectionField, Function<ResortRoomFacilityGroupFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomFacilityGroupSearchField::isLocaleField)
                .map(ResortRoomFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }
}
