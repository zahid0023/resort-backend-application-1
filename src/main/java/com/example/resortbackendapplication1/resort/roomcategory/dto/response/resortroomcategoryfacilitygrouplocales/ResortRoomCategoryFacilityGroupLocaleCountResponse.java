package com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryfacilitygrouplocales;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryFacilityGroupLocaleCountResponse {
    private final Long count;
    private final List<String> codes;

    public ResortRoomCategoryFacilityGroupLocaleCountResponse(final Long count, final List<String> codes) {
        this.count = count;
        this.codes = codes;
    }
}
