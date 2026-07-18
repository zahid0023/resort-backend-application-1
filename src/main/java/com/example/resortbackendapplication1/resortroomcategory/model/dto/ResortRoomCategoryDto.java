package com.example.resortbackendapplication1.resortroomcategory.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryDto {
    private Long id;
    private Long resortId;
    private Long roomCategoryId;
    private String code;
    private Integer sortOrder;
    private Integer maxAdults;
    private Integer maxChildren;
    private Integer maxOccupancy;
    private LocalTime defaultCheckInTime;
    private LocalTime defaultCheckOutTime;
    private Boolean isExtraBedAllowed;
    private Integer maxExtraBeds;
    private Boolean isSmokingAllowed;
    private Boolean isPetsAllowed;
    private List<ResortRoomCategoryLocaleDto> locales;
}
