package com.example.resortbackendapplication1.resort.roomcategory.model.dto;

import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryBedDto {

    private Long id;

    private ResortRoomCategoryDto resortRoomCategory;

    private BedTypeDto bedType;

    private Integer quantity;

    private Boolean isExtraBedAllowed;

    private Integer maxExtraBeds;
}
