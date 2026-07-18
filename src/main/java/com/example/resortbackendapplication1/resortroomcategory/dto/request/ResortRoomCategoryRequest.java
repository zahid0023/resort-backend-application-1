package com.example.resortbackendapplication1.resortroomcategory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryRequest {

    @NotNull
    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder;

    @NotNull
    @Min(value = 1, message = "max_adults must be at least 1")
    private Integer maxAdults;

    @NotNull
    @Min(value = 0, message = "max_children must be 0 or greater")
    private Integer maxChildren;

    @NotNull
    @Min(value = 1, message = "max_occupancy must be at least 1")
    private Integer maxOccupancy;

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
}
