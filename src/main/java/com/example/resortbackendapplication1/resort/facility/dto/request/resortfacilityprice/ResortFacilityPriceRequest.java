package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityPriceRequest {

    /** Monetary amount. Omit/null for FREE/INCLUDED pricing types. */
    private BigDecimal amount;

    private String note;
}
