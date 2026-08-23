package com.example.resortbackendapplication1.resort.model.enums;

import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ResortRoomCategoryFacilityGroupSearchField implements SearchFieldSpec<ResortRoomCategoryFacilityGroupFilterRequest> {
    CODE("code", SearchType.LIKE, false, null, ResortRoomCategoryFacilityGroupFilterRequest::getCode),
    NAME("name", SearchType.LIKE, true, "resortRoomCategoryFacilityGroupLocaleEntities", ResortRoomCategoryFacilityGroupFilterRequest::getName);

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<ResortRoomCategoryFacilityGroupFilterRequest, String> valueExtractor;

    ResortRoomCategoryFacilityGroupSearchField(String fieldName, SearchType searchType, boolean localeField,
                                               String collectionField, Function<ResortRoomCategoryFacilityGroupFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values()).map(ResortRoomCategoryFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values()).filter(ResortRoomCategoryFacilityGroupSearchField::isLocaleField)
                .map(ResortRoomCategoryFacilityGroupSearchField::getFieldName).collect(Collectors.toSet());
    }
}
