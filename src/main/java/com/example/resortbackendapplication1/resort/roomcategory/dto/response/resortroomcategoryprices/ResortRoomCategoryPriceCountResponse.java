package com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceCountResponse {
    private final Long count;
    private final List<String> codes;

    public ResortRoomCategoryPriceCountResponse(final Long count, final List<String> codes) {
        this.count = count;
        this.codes = codes;
    }
}
