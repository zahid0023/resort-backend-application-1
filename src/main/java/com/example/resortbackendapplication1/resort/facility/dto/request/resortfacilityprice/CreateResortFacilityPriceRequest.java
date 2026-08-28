package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityPriceRequest extends ResortFacilityPriceRequest {

    /** Pricing classification (FREE/INCLUDED/FIXED/VARIABLE). Immutable after creation. */
    @NotNull
    private Long priceTypeId;

    /** Billing unit (PER_PERSON/PER_ROOM/...). Null for FREE/INCLUDED pricing. Immutable after creation. */
    private Long priceUnitId;

    /** Currency of {@code amount}. Immutable after creation. */
    @NotNull
    private Long currencyId;
}
