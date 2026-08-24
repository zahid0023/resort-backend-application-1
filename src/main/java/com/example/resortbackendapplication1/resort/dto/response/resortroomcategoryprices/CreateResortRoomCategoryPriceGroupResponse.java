package com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryPriceGroupResponse {
    private final Boolean success;
    private final Long basePriceId;
    private final Long weekdayPriceId;
    private final Long weekendPriceId;

    public CreateResortRoomCategoryPriceGroupResponse(final Boolean success, final Long basePriceId,
                                                       final Long weekdayPriceId, final Long weekendPriceId) {
        this.success = success;
        this.basePriceId = basePriceId;
        this.weekdayPriceId = weekdayPriceId;
        this.weekendPriceId = weekendPriceId;
    }
}
