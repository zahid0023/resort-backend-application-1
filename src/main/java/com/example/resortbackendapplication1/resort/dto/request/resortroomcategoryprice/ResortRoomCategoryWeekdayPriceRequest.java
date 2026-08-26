package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Days of week are no longer submitted per currency here — WEEKDAY's day-of-week set is shared by every
 * currency (and every room category) at the resort, set via
 * {@code PUT /resorts/{resort-id}/weekly-schedule}. This class stays a distinct (currently empty) type, not
 * collapsed into {@link ResortRoomCategoryPriceRequest}, purely so {@code MainPriceRequest}'s JSON keeps its
 * named {@code weekday_price} field.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryWeekdayPriceRequest extends ResortRoomCategoryPriceRequest {
}
