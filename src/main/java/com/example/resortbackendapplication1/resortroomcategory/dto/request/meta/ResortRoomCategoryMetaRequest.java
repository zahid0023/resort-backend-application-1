package com.example.resortbackendapplication1.resortroomcategory.dto.request.meta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMetaRequest {

    @NotNull
    @Min(value = 1, message = "max_adults must be at least 1")
    private Integer maxAdults;

    @NotNull
    @Min(value = 0, message = "max_children must be 0 or greater")
    private Integer maxChildren;

    @NotNull
    @Min(value = 0, message = "max_infants must be 0 or greater")
    private Integer maxInfants;

    @NotNull
    @Min(value = 1, message = "max_occupancy must be at least 1")
    private Integer maxOccupancy;

    @Positive(message = "room_size must be greater than 0")
    private BigDecimal roomSize;

    private Long roomSizeUnitId;

    @NotNull
    @Min(value = 1, message = "bedroom_count must be at least 1")
    private Integer bedroomCount;

    @NotNull
    @Min(value = 1, message = "bathroom_count must be at least 1")
    private Integer bathroomCount;

    private LocalTime defaultCheckInTime;

    private LocalTime defaultCheckOutTime;

    @NotNull
    private Boolean isExtraBedAllowed;

    @NotNull
    @Min(value = 0, message = "max_extra_beds must be 0 or greater")
    private Integer maxExtraBeds;

    @NotNull
    private Boolean isSmokingAllowed;

    @NotNull
    private Boolean isPetsAllowed;

    @NotNull
    @Min(value = 1, message = "minimum_stay_nights must be at least 1")
    private Integer minimumStayNights;

    @Min(value = 1, message = "maximum_stay_nights must be at least 1")
    private Integer maximumStayNights;
}
